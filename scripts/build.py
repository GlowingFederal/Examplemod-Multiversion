#!/usr/bin/env python3
"""Dispatch pinned wrappers and coordinate a repository-wide production transaction."""
import argparse
import json
import os
from pathlib import Path
import re
import subprocess
import struct
import sys
import tempfile
import zipfile

ROOT = Path(__file__).resolve().parents[1]
VERSION_FILE = ROOT / "version.properties"
VERSIONS = ("1.6.4", "1.7.10", "1.8.9", "1.12.2", "1.16.5", "1.18.2", "1.20.1", "1.21.11")


def read_version():
    if not VERSION_FILE.exists():
        VERSION_FILE.write_text("mod_version=1.0.0\nbuild_number=0\n", encoding="utf-8")
    original = VERSION_FILE.read_bytes()
    text = original.decode("utf-8")
    semantic = [v.strip() for v in re.findall(r"(?m)^[ \t]*mod_version[ \t]*[:=][ \t]*([^\r\n]*)", text)]
    number = [v.strip() for v in re.findall(r"(?m)^[ \t]*build_number[ \t]*[:=][ \t]*([^\r\n]*)", text)]
    if len(semantic) != 1 or not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+(?:-[0-9A-Za-z.-]+)?", semantic[0]):
        raise ValueError("version.properties must contain one semantic mod_version (e.g. 1.0.0)")
    if len(number) != 1 or not re.fullmatch(r"[0-9]+", number[0]) or int(number[0]) >= 2**63 - 1:
        raise ValueError("version.properties must contain one non-negative build_number below Long.MAX_VALUE")
    return original, semantic[0], int(number[0])


def persist_version(original, number):
    if VERSION_FILE.read_bytes() != original:
        raise RuntimeError("version.properties changed during the build; refusing to overwrite it")
    updated = re.sub(rb"(?m)^(\s*build_number\s*[:=]\s*)[0-9]+", lambda m: m[1] + str(number).encode(), original, count=1)
    fd, name = tempfile.mkstemp(prefix=".version-", suffix=".tmp", dir=ROOT)
    try:
        with os.fdopen(fd, "wb") as stream:
            stream.write(updated)
            stream.flush()
            os.fsync(stream.fileno())
        os.replace(name, VERSION_FILE)
    finally:
        if os.path.exists(name):
            os.unlink(name)


def run(directory, tasks, release=None, warning_mode=None):
    wrapper = directory / ("gradlew.bat" if os.name == "nt" else "gradlew")
    command = [str(wrapper)] if os.name == "nt" else ["sh", str(wrapper)]
    command += tasks + ["--console=plain", "--max-workers=2"]
    if release:
        command += [f"-PreleaseModVersion={release[0]}", f"-PreleaseBuildNumber={release[1]}"]
    if warning_mode:
        command += ["--warning-mode", warning_mode]
    print(f"Building {directory.name}: {' '.join(tasks)}", flush=True)
    return subprocess.call(command, cwd=directory)


def verification_settings(mc, full_version):
    """Resolve project expectations without allocating or persisting a version."""
    project = json.loads((ROOT / "verification.json").read_text(encoding="utf-8"))
    runtime = json.loads((ROOT / "versions" / mc / "target.json").read_text(encoding="utf-8"))
    target = project["targets"][mc]
    values = {**project, **runtime, **target, "mc": mc, "full_version": full_version,
              "package_path": project["base_package"].replace(".", "/")}

    def expand(value):
        if isinstance(value, str):
            return value.format_map(values)
        if isinstance(value, list):
            return [expand(item) for item in value]
        if isinstance(value, dict):
            return {expand(key): expand(item) for key, item in value.items()}
        return value

    settings = expand({key: value for key, value in project.items()
                       if key not in ("targets", "metadata_profiles")})
    settings.update(expand(target))
    # Per-target content extends the shared requirements, rather than replacing them.
    for key in ("required_classes", "required_shared_classes", "required_assets"):
        settings[key] = expand(project.get(key, []) + target.get(key, []))
    settings["metadata"] = expand(project["metadata_profiles"][target["metadata_profile"]])
    settings["target_java"] = runtime["target_java"]
    remapping = settings["remapping"]
    if remapping["required"] and not remapping["checks"]:
        raise ValueError(f"{mc}: remapping requires at least one bytecode symbol check")
    return settings


def class_info(data):
    """Read exact UTF-8 constant-pool symbols, not coincidental byte substrings."""
    if len(data) < 10 or data[:4] != b"\xca\xfe\xba\xbe":
        raise RuntimeError("Invalid classfile header")
    count = int.from_bytes(data[8:10], "big")
    symbols, offset, index = set(), 10, 1
    sizes = {3: 4, 4: 4, 5: 8, 6: 8, 7: 2, 8: 2, 9: 4, 10: 4, 11: 4,
             12: 4, 15: 3, 16: 2, 17: 4, 18: 4, 19: 2, 20: 2}
    while index < count:
        if offset >= len(data):
            raise RuntimeError("Truncated classfile constant pool")
        tag = data[offset]
        offset += 1
        if tag == 1:
            length = struct.unpack_from(">H", data, offset)[0]
            offset += 2
            symbols.add(data[offset:offset + length].decode("utf-8", errors="replace"))
            offset += length
        elif tag in sizes:
            offset += sizes[tag]
            if tag in (5, 6):
                index += 1
        else:
            raise RuntimeError(f"Unknown classfile constant tag: {tag}")
        if offset > len(data):
            raise RuntimeError("Truncated classfile constant pool")
        index += 1
    return int.from_bytes(data[6:8], "big"), symbols


def check_json(text, expectations, label):
    resource = json.loads(text)
    for key, expected in expectations.items():
        actual = resource
        try:
            for part in key.split("."):
                actual = actual[int(part)] if isinstance(actual, list) else actual[part]
        except (KeyError, IndexError, TypeError, ValueError):
            raise RuntimeError(f"{label} is missing JSON field {key}") from None
        if actual != expected:
            raise RuntimeError(f"{label}: {key} is {actual!r}, expected {expected!r}")


def check_metadata(text, metadata, label):
    if "${" in text:
        raise RuntimeError(f"{label} contains unexpanded metadata tokens")
    if "json_values" in metadata:
        check_json(text, metadata["json_values"], label)
    # Match all requested fields within the same TOML table instance, including
    # repeated dependency tables. This does not require Python 3.11's tomllib.
    sections = [("", text.split("[", 1)[0])]
    headers = list(re.finditer(r"(?m)^\s*\[\[?([^\]\r\n]+)\]\]?\s*$", text))
    if headers:
        sections[0] = ("", text[:headers[0].start()])
    for index, header in enumerate(headers):
        end = headers[index + 1].start() if index + 1 < len(headers) else len(text)
        sections.append((header[1], text[header.end():end]))
    for check in metadata.get("sections", []):
        def matches(body):
            for field, expected in check["fields"].items():
                literal = str(expected).lower() if isinstance(expected, bool) else f'"{expected}"'
                if not re.search(r"(?m)^\s*" + re.escape(field) + r"\s*=\s*" +
                                 re.escape(literal) + r"\s*(?:#.*)?$", body):
                    return False
            return True
        if not any(table == check["table"] and matches(body) for table, body in sections):
            raise RuntimeError(f"{label} has inconsistent [{check['table']}] fields: {check['fields']}")


def verify(mc, full_version, directory=None):
    config = verification_settings(mc, full_version)
    filename = config["archive_pattern"]
    if Path(filename).name != filename or not filename.endswith(".jar") or filename.endswith("-dev.jar"):
        raise ValueError("archive_pattern must name a production JAR, without directories or -dev suffix")
    jar = (directory or ROOT / "versions" / mc / "build" / "libs") / filename
    if not jar.is_file():
        raise RuntimeError(f"Missing required distributable: {jar}")
    with zipfile.ZipFile(jar) as archive:
        names = archive.namelist()
        if len(names) != len(set(names)):
            raise RuntimeError(f"{jar.name} contains duplicate archive entries")
        required_classes = config["required_classes"] + config["required_shared_classes"] + [config["version_class"]]
        required = required_classes + config["required_assets"] + [config["metadata"]["path"], "META-INF/MANIFEST.MF"]
        required += [check["path"] for check in config["json_checks"]]
        required += [check["class"] for check in config["remapping"]["checks"]]
        for path in required:
            if path not in names:
                raise RuntimeError(f"{jar.name} is missing {path}")
        classes = {}
        for path in names:
            if path.endswith(".json") and any(path.startswith(root) for root in config["resource_roots"]):
                resource = archive.read(path).decode("utf-8")
                if "${" in resource:
                    raise RuntimeError(f"{jar.name} contains unexpanded resource tokens: {path}")
                json.loads(resource)
            if mc == "1.6.4" and (path.endswith(".jar") or
                    (path.endswith(".class") and not any(path.startswith(root) for root in config["class_roots"]))):
                raise RuntimeError(f"{jar.name} contains an unexpected bundled dependency: {path}")
            if path.endswith(".class"):
                major, symbols = class_info(archive.read(path))
                expected = config["target_java"] + 44
                if major > expected or (path in required_classes and major != expected):
                    raise RuntimeError(f"{path} has class version {major}, target expects {expected}")
                classes[path] = symbols
        if full_version not in classes[config["version_class"]]:
            raise RuntimeError(f"{jar.name} has an inconsistent generated version constant")
        for check in config["remapping"]["checks"]:
            symbols = classes[check["class"]]
            if not set(check["present"]).issubset(symbols) or set(check["absent"]) & symbols:
                raise RuntimeError(f"{jar.name} failed remapping symbol checks in {check['class']}")
        for check in config["json_checks"]:
            check_json(archive.read(check["path"]).decode("utf-8"), check["values"], check["path"])
        check_metadata(archive.read(config["metadata"]["path"]).decode("utf-8"), config["metadata"], jar.name)
        manifest = archive.read("META-INF/MANIFEST.MF").decode("utf-8").replace("\r\n", "\n").replace("\n ", "")
        if mc == "1.6.4" and any(line.startswith(("Class-Path:", "FMLCorePlugin:", "Premain-Class:", "Agent-Class:"))
                                   for line in manifest.splitlines()):
            raise RuntimeError(f"{jar.name} declares an unexpected launch/bootstrap dependency")
        for field, value in (("Implementation-Version", full_version), ("Implementation-Title", config["display_name"])):
            if f"{field}: {value}" not in manifest.splitlines():
                raise RuntimeError(f"{jar.name} has inconsistent manifest {field}")
    print(f"Verified {jar.name} (Java {config['target_java']})", flush=True)
    return jar


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=("build", "all", "test", "client", "server", "plan", "verify", "persist"))
    parser.add_argument("target", nargs="?", choices=VERSIONS)
    parser.add_argument("--full-version")
    parser.add_argument("--artifacts", type=Path)
    parser.add_argument("--number", type=int)
    parser.add_argument("--skip-common-tests", action="store_true",
                        help="For root Gradle launchers whose :common:test prerequisite already passed")
    parser.add_argument("--dry-run", action="store_true", help="Inspect the native client/server task graph without launching")
    parser.add_argument("--warning-mode", choices=("all", "summary", "fail"),
                        help="Forward Gradle diagnostics to every invoked wrapper without filtering output")
    args = parser.parse_args()
    if args.skip_common_tests and args.action not in ("all", "test"):
        parser.error("--skip-common-tests is only valid for all or test")
    if args.dry_run and args.action not in ("client", "server"):
        parser.error("--dry-run is only valid for client or server")
    if args.action in ("client", "server"):
        if not args.target:
            parser.error(f"{args.action} requires a target")
        tasks = ["runClient" if args.action == "client" else "runServer"]
        if args.dry_run:
            tasks.append("--dry-run")
        return run(ROOT / "versions" / args.target, tasks, warning_mode=args.warning_mode)
    original, semantic, previous = read_version()
    if args.action == "plan":
        print(json.dumps({"mod_version": semantic, "build_number": previous + 1, "full_version": f"{semantic}.{previous + 1}"}))
        return 0
    if args.action == "verify":
        if not args.target or not args.full_version:
            parser.error("verify requires target and --full-version")
        verify(args.target, args.full_version, args.artifacts)
        return 0
    if args.action == "persist":
        if args.number != previous + 1 or args.artifacts is None:
            parser.error("persist requires --number equal to build_number + 1 and --artifacts containing all eight jars")
        for mc in VERSIONS:
            verify(mc, f"{semantic}.{args.number}", args.artifacts)
        persist_version(original, args.number)
        return 0
    if args.action == "test":
        status = 0 if args.skip_common_tests else run(ROOT, [":common:test"], warning_mode=args.warning_mode)
        for mc in VERSIONS:
            status = run(ROOT / "versions" / mc, ["test"], warning_mode=args.warning_mode) or status
        return status
    if args.action == "build":
        if not args.target:
            parser.error("build requires a target")
        status = run(ROOT / "versions" / args.target, ["build"], warning_mode=args.warning_mode)
        if status == 0:
            _, semantic, number = read_version()
            verify(args.target, f"{semantic}.{number}")
        return status
    lock = ROOT / ".build-number.lock"
    try:
        lock.mkdir()
    except FileExistsError:
        raise RuntimeError("Another production build owns .build-number.lock")
    try:
        # Re-read while holding the same lock used by direct Gradle builds.
        original, semantic, previous = read_version()
        status = 0 if args.skip_common_tests else run(ROOT, [":common:test"], warning_mode=args.warning_mode)
        if status:
            return status
        failures = []
        for mc in VERSIONS:
            result = run(ROOT / "versions" / mc, ["build"], (semantic, previous + 1), warning_mode=args.warning_mode)
            if result:
                failures.append(mc)
            else:
                try:
                    verify(mc, f"{semantic}.{previous + 1}")
                except (RuntimeError, zipfile.BadZipFile) as error:
                    print(error, file=sys.stderr)
                    failures.append(mc)
        if failures:
            print("Production build failed: " + ", ".join(failures) + "; build_number preserved", file=sys.stderr)
            return 1
        persist_version(original, previous + 1)
        return 0
    finally:
        lock.rmdir()


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (ValueError, RuntimeError, OSError, KeyError, zipfile.BadZipFile, struct.error) as error:
        print(f"Multiversion build: {error}", file=sys.stderr)
        sys.exit(1)
