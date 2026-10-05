#!/usr/bin/env python3
"""Dispatch pinned wrappers and coordinate a repository-wide production transaction."""
import argparse
import json
import os
from pathlib import Path
import re
import subprocess
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
    semantic = re.findall(r"(?m)^\s*mod_version\s*[:=]\s*(\S+)\s*$", text)
    number = re.findall(r"(?m)^\s*build_number\s*[:=]\s*([0-9]+)[ \t]*\r?$", text)
    if len(semantic) != 1 or not re.fullmatch(r"[0-9]+\.[0-9]+\.[0-9]+(?:-[0-9A-Za-z.-]+)?", semantic[0]):
        raise ValueError("version.properties must contain one semantic mod_version (e.g. 1.0.0)")
    if len(number) != 1 or int(number[0]) >= 2**63 - 1:
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


def run(directory, tasks, release=None):
    wrapper = directory / ("gradlew.bat" if os.name == "nt" else "gradlew")
    command = [str(wrapper)] if os.name == "nt" else ["sh", str(wrapper)]
    command += tasks + ["--console=plain", "--max-workers=2"]
    if release:
        command += [f"-PreleaseModVersion={release[0]}", f"-PreleaseBuildNumber={release[1]}"]
    print(f"Building {directory.name}: {' '.join(tasks)}", flush=True)
    return subprocess.call(command, cwd=directory)


def verify(mc, full_version, directory=None):
    config = json.loads((ROOT / "versions" / mc / "target.json").read_text())
    jar = (directory or ROOT / "versions" / mc / "build" / "libs") / f"examplemod-{full_version}+mc{mc}-{config['loader']}.jar"
    if not jar.is_file():
        raise RuntimeError(f"Missing required distributable: {jar}")
    with zipfile.ZipFile(jar) as archive:
        required = ("com/glowingfederal/examplemod/ExampleMod.class", "com/glowingfederal/examplemod/BuildVersion.class",
                    "com/glowingfederal/examplemod/domain/ExampleGreeting.class",
                    "assets/examplemod/textures/blocks/example_block.png", "assets/examplemod/textures/items/example_item.png")
        for path in required:
            if path not in archive.namelist():
                raise RuntimeError(f"{jar.name} is missing {path}")
        metadata = "META-INF/neoforge.mods.toml" if config['loader'] == "neoforge" else "mcmod.info" if mc in VERSIONS[:4] else "META-INF/mods.toml"
        if full_version not in archive.read(metadata).decode():
            raise RuntimeError(f"{jar.name} has inconsistent mod metadata")
        manifest = archive.read("META-INF/MANIFEST.MF").decode()
        if f"Implementation-Version: {full_version}" not in manifest:
            raise RuntimeError(f"{jar.name} has inconsistent manifest version")
        for path in required[:3]:
            data = archive.read(path)
            major = int.from_bytes(data[6:8], "big")
            expected = config["target_java"] + 44
            if major > expected:
                raise RuntimeError(f"{path} requires class version {major}, target permits {expected}")
    print(f"Verified {jar.name}", flush=True)
    return jar


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=("build", "all", "test", "plan", "verify", "persist"))
    parser.add_argument("target", nargs="?", choices=VERSIONS)
    parser.add_argument("--full-version")
    parser.add_argument("--artifacts", type=Path)
    parser.add_argument("--number", type=int)
    args = parser.parse_args()
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
        status = run(ROOT, [":common:test"])
        for mc in VERSIONS:
            status = run(ROOT / "versions" / mc, ["test"]) or status
        return status
    if args.action == "build":
        if not args.target:
            parser.error("build requires a target")
        status = run(ROOT / "versions" / args.target, ["build"])
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
        status = run(ROOT, [":common:test"])
        if status:
            return status
        failures = []
        for mc in VERSIONS:
            result = run(ROOT / "versions" / mc, ["build"], (semantic, previous + 1))
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
    except (ValueError, RuntimeError, OSError) as error:
        print(f"ExampleMod: {error}", file=sys.stderr)
        sys.exit(1)
