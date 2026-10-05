# ExampleMod

A standalone Forge/NeoForge example maintained in one repository on `main`. It adds an Example Block, an Example Item and the public server command `/examplemod`, which greets the sender using the same portable domain function on every target. Console senders work too. Install only the JAR matching your Minecraft version and loader. No GTNH modpack, GTNH runtime library or Fabric installation is required.

## Support and tooling

All eight targets compile, run the five shared JUnit vectors, and produce inspected production JARs. The root common project passes the same tests. Archive checks cover metadata, generated versions, bytecode levels and resource references. The GitHub workflows pass local actionlint checks; they have not yet run on GitHub. In-game rendering, commands, client launches and dedicated-server behavior still need runtime testing.

| Minecraft | Loader pin | Build tooling pin | Wrapper | Build JDK | Source / runtime Java | Build status |
|---|---|---|---|---:|---|---|
| 1.6.4 | Forge 9.11.1.1345 | Unimined 1.4.1 | 8.13 | 21 | 8 / 8 | Build verified |
| 1.7.10 | Forge 10.13.4.1614 | GTNHGradle 2.0.34, RetroFuturaGradle 2.0.6 | 9.7.1 | 25 | 17 with Jabel / 8 | Build verified |
| 1.8.9 | Forge 11.15.1.2318 | Unimined 1.4.1 | 8.13 | 21 | 8 / 8 | Build verified |
| 1.12.2 | Forge 14.23.5.2864 | Unimined 1.4.1 | 8.13 | 21 | 8 / 8 | Build verified |
| 1.16.5 | Forge 36.2.42 | ForgeGradle 6.0.54 | 8.13 | 21 | 8 / 8 | Build verified |
| 1.18.2 | Forge 40.3.12 | ModDevGradle Legacy Forge 2.0.148 | 8.13 | 21 | 17 / 17 | Build verified |
| 1.20.1 | Forge 47.4.10 | ModDevGradle Legacy Forge 2.0.148 | 8.13 | 21 | 17 / 17 | Build verified |
| 1.21.11 | NeoForge 21.11.45 | ModDevGradle 2.0.148 | 9.2.1 | 21 | 21 / 21 | Build verified |

Tooling was selected per generation, considering current maintained replacements and historical official builds:

- **1.6.4:** [Unimined's upstream Forge example](https://github.com/unimined/Unimined/tree/lts/1.4/testing/1.6.4-Forge) supports the Java 8-compatible Forge 1345 release. Its FG2-compatible transformer uses modern downloads/remapping without requiring an abandoned ForgeGradle 1.0 process or a private patched toolchain. The original FG1 userdev exists for 964, but not 1345. The older Forge version and dead HTTP endpoints make that path a poorer baseline.
- **1.7.10:** [GTNH ExampleMod](https://github.com/GTNewHorizons/ExampleMod1.7.10) is the reference. GTNHGradle supplies RetroFuturaGradle, maintained repositories, IDE support, generic injection and Jabel. Git-derived versioning, automatic buildscript updates and unused publishing/mixin integrations are disabled so the root version file remains authoritative. The pinned upstream plugin uses JDK 25 to run; **Jabel compiles Java 17 syntax against Java 8 APIs and bytecode**. Native modern bytecode is not enabled. JVM Downgrader is unnecessary for this domain function; reconsider it if an adapter needs newer library APIs, auditing bundled stubs and licensing first. Shared code stays within Java 8 syntax/API because the other adapters compile it directly.
- **1.8.9:** Unimined's FG2-compatible transformer supports this exact Forge generation and stable MCP 22 mappings. It replaces FG2.1's obsolete build environment while preserving a normal Forge output. GTNH's 1.7.10-specific conventions are not applied to this target.
- **1.12.2:** Unimined supports FG2-compatible Forge builds with stable MCP 39 mappings and the maintained Forge 2864 release. The anatawa12 FG2.3 fork was evaluated as a historical alternative; Unimined avoids the old Gradle/dependency setup while keeping Java 8 output. [The upstream Forge integration example](https://github.com/unimined/Unimined/tree/lts/1.4/testing/1.12.2-Forge-Fabric-Liteloader) also supports other loaders; this repository enables Forge only.
- **1.16.5:** Current official ForgeGradle remains appropriate. [ModDevGradle Legacy Forge](https://github.com/neoforged/ModDevGradle/blob/main/LEGACY.md) starts at Minecraft 1.17, so it does not support this target. ForgeGradle's JDK 8 toolchain compiles the adapter; JDK 21 runs Gradle.
- **1.18.2 and 1.20.1:** ModDevGradle's maintained Legacy Forge plugin supports these Forge targets, official Mojang mappings and production SRG reobfuscation. It shares conventions with the NeoForge target and avoids keeping another ForgeGradle-specific configuration where unnecessary. The runtime loader remains Forge.
- **1.21.11:** [The current NeoForge MDK](https://github.com/NeoForgeMDKs/MDK-1.21.11-ModDevGradle) provides the selected loader, Java 21, wrapper and ModDevGradle generation. NeoForge needs no mod reobfuscation pass.

Each `versions/<minecraft>/target.json` records its selected toolchain. Dependencies and wrapper distribution checksums are pinned. Builds may still resolve loader-owned transitive dependencies according to the upstream loader metadata.

## Repository layout

```text
common/                 Portable Java 8 domain sources, assets and JUnit tests
versions/<minecraft>/   Isolated build, entry point, registries, command/chat API and resources
build-logic/            Shared source/resource, packaging and version transaction conventions
scripts/                JDK selection, wrapper dispatch and aggregate release coordinator
.github/workflows/      Independent matrix builds and eight-target tag releases
version.properties      One manually managed semantic version and persistent build counter
```

The root configures **only `common`**. Every target has its own settings and wrapper. No old or new loader plugin has to coexist with another target's plugin in a Gradle daemon. Modern adapters share build conventions where their tooling supports them. No permanent version branches or copied domain code are needed.

`common` contains algorithms, data/configuration/payload models, validation and gameplay calculations. It has no Minecraft, Forge, NeoForge or Fabric dependencies. Narrow platform interfaces can be added when shared logic needs a service; do not build a universal Minecraft facade. For this greeting, returning a string is the complete boundary:

```text
version command -> ExampleGreeting.create(senderName) -> version chat API
```

Keep registries, blocks/items/entities, world hooks, network authority, lifecycle, commands, screens and rendering in adapters. A future `versions/1.20.1-fabric` can compile the same common sources without rewriting the domain layer. Stonecutter is unnecessary for the current eight small adapters: the API differences span different registration/lifecycle generations rather than a few signatures.

Textures and compatible model templates live once in `common/assets`; builds emit the legacy plural or modern singular texture paths required by the game atlases. Language values live once in `common/assets/en_us.properties`; builds emit `.lang` or JSON and the appropriate key names. The item-model parent is selected for 1.8.9's builtin format or later versions' item format. Blockstates, loader metadata, loot-table paths and pack formats belong to each adapter, including 1.21.11's item definition files and min/max pack format metadata. Modern blocks drop their matching block item. Common sources are compiled into each mod JAR, so players do not install a separate common library. Tests are compiled and run against each target as well as the root common project.

## Building

Install full **JDKs 8, 17, 21 and 25** for all targets and development runs. Root/all commands also need **Python 3.9+**; direct Gradle wrappers do not. No global Gradle is used.

Wrappers select their build JDK automatically from `JAVA8_HOME`, `JAVA17_HOME`, `JAVA21_HOME`, `JAVA25_HOME`, `JAVA_HOME`, `~/.jdks` and standard JDK installation directories. Explicit per-generation variables are optional overrides. Toolchain resolvers can download missing compiler JDKs for targets that support them. IDEs must use the table's build JDK for the selected adapter; wrapper selection does not configure an IDE's own Gradle JVM.

Build one target:

```sh
./scripts/build-version.sh 1.7.10
# Or independently, without configuring the root build:
cd versions/1.7.10
./gradlew build
```

Windows:

```powershell
./scripts/build-version.ps1 1.20.1
# From the target directory:
./gradlew.bat build
```

Build all eight with one production number:

```sh
./scripts/build-all.sh
./scripts/test-all.sh
./gradlew :common:test
```

Windows equivalents are `scripts/build-all.ps1`, `scripts/test-all.ps1` and `gradlew.bat :common:test`. Override the coordinator Python executable with `PYTHON` if required. Builds use the target's wrapper; the coordinator does not load target projects into the root build. Unimined compiles with JDK 21 and `--release 8`; the game runs on JDK 8. ForgeGradle 1.16.5 uses its JDK 8 compiler, and GTNH Jabel uses its JDK 17 compiler. First builds download game/tooling dependencies and can take several minutes.

Artifacts are under `versions/<minecraft>/build/libs/`, named `examplemod-1.0.0.42+mc1.7.10-forge.jar`, for example. Developer archives, if generated, have a `-dev` suffix and must not be distributed. The coordinator and CI inspect required classes, shared assets, metadata/manifest versions and classfile runtime levels in the production archive.

## Development clients and servers

From any target directory, use `./gradlew runClient` or `./gradlew runServer` (`gradlew.bat` on Windows). These tasks do not advance the build number. Server runs need their own Minecraft EULA acceptance. The wrappers and Java toolchains keep the old game's Java runtime separate from the Java used by Gradle.

GTNH exposes `setupDecompWorkspace` through its legacy task emulation and modern IDE tasks; its build sets up the decompiled game automatically. Unimined exposes `genSources` and IntelliJ run configurations instead of FG1/FG2's workspace setup task. ForgeGradle 1.16.5 exposes `genIntellijRuns` / `genEclipseRuns`. ModDevGradle creates development runs during IDE import.

For 1.6.4, numeric registry IDs are configurable in the mod's suggested configuration file (defaults: block 3000, item constructor ID 12000). Do not change these in an existing world without migrating its IDs. In game, check the creative inventory or `/give` for `examplemod:example_item` and `examplemod:example_block` (numeric IDs apply to 1.6.4), then run `/examplemod`. Test both a dedicated server and a client before considering runtime behavior verified.

## Versions and production transactions

`version.properties` starts with:

```properties
mod_version=1.0.0
build_number=0
```

Only humans change `mod_version`. Production packaging uses `build_number + 1`; metadata, generated `BuildVersion`, manifest and filename use `fullVersion = mod_version.build_number`. There are no target-local version files.

An explicitly requested `build`, `jar`, `assemble`, `reobfJar` or `remapJar` allocates once per invocation, even when tasks depend on each other. `jar` completes the target's remapping too. Compilation, resource processing, tests/checks, runs, IDE import, workspace setup, dependency resolution and clean do not allocate. A failed build preserves the persistent counter. Comments, other keys, formatting and line endings survive a successful update.

Direct builds lock the root counter and commit it after successful packaging. `build-all` holds that same lock, passes one resolved number to all eight wrappers, verifies every artifact, and commits only after every target succeeds. Failure can leave ignored partial JAR outputs; those are not a complete release. Concurrent local production builds fail clearly instead of racing. If a process is forcibly killed, remove the empty `.build-number.lock` directory only after confirming no production build is running. Configuration caching is disabled because version transactions depend on the requested tasks and build outcome.

CI pull-request/main builds use one planned candidate number without changing the repository. Tag releases use `v<next-fullVersion>` (for example, `v1.0.0.1` when the root counter is 0). The release matrix uses that one number on every runner. Only after common tests, all eight builds and archive verification succeed does the release workflow advance `version.properties` on `main` and upload a combined release artifact. Enable Actions write access for that final job; a protected `main` may require an approved bot path. A changed counter or semantic version on `main` makes persistence fail instead of overwriting newer work. The workflow produces artifacts; it does not publish to mod hosting services.

For a local successful production build, include the updated `version.properties` in the normal human-reviewed source commit. Avoid using an already consumed local production number as the next CI tag.

## Adding a target or feature

1. Decide whether the behavior is portable domain logic or a Minecraft integration.
2. Add portable code and tests once under `common`; keep its syntax and APIs compatible with Java 8.
3. For a new target, evaluate maintained tooling against that generation's official tooling, pin the compatible loader/plugin/wrapper/JDK and add `target.json`.
4. Implement the integration under its own `versions/<target>` sources. Include shared/version build conventions and ensure packaging outputs the remapped archive expected by `scripts/build.py`.
5. Supply only the target-specific resource formats, then add the target to the coordinator and CI matrix. Build, inspect the JAR and run client/dedicated-server checks.

EditorConfig defines four-space Java indentation and two-space JSON/YAML indentation. Preserve loader-specific APIs and keep common independent. Generated IDE data, caches, JAR outputs and logs are ignored. The checked-in official Gradle bootstrap JARs are the small wrapper exception. Existing Forge/FML license and credit files remain as third-party notices; ExampleMod source is MIT licensed.
