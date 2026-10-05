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
- **1.12.2:** Unimined uses its FG3-compatible transformer for the maintained Forge 2864 release, with the SRG config supplied by Forge userdev and stable MCP 39 names. The anatawa12 FG2.3 fork was evaluated as a historical alternative; Unimined avoids the old Gradle/dependency setup while keeping Java 8 output. [The upstream Forge integration example](https://github.com/unimined/Unimined/tree/lts/1.4/testing/1.12.2-Forge-Fabric-Liteloader) also supports other loaders; this repository enables Forge only.
- **1.16.5:** Current official ForgeGradle remains appropriate. [ModDevGradle Legacy Forge](https://github.com/neoforged/ModDevGradle/blob/main/LEGACY.md) starts at Minecraft 1.17, so it does not support this target. ForgeGradle's JDK 8 toolchain compiles the adapter; JDK 21 runs Gradle.
- **1.18.2 and 1.20.1:** ModDevGradle's maintained Legacy Forge plugin supports these Forge targets, official Mojang mappings and production SRG reobfuscation. It shares conventions with the NeoForge target and avoids keeping another ForgeGradle-specific configuration where unnecessary. The runtime loader remains Forge.
- **1.21.11:** [The current NeoForge MDK](https://github.com/NeoForgeMDKs/MDK-1.21.11-ModDevGradle) provides the selected loader, Java 21, wrapper and ModDevGradle generation. NeoForge needs no mod reobfuscation pass.

Each `versions/<minecraft>/target.json` records its selected toolchain. Dependencies and wrapper distribution checksums are pinned. Builds may still resolve loader-owned transitive dependencies according to the upstream loader metadata.

### Build warning audit

Normal builds retain Gradle's warning reporting, compiler diagnostics, dependency/remapping errors and ordinary task/cache output. No console filtering or global warning suppression is used. The audit of the pinned tool families found:

| Message / family | Owner | Handling |
|---|---|---|
| Obsolete Java 8 source/target options on Unimined's JDK 21 compiler | GRADLE/TOOL INTERNAL (javac compatibility diagnostic) | Only `-Xlint:-options` is added to Java compilation in 1.6.4, 1.8.9 and 1.12.2. Java 8 targeting and other warning categories remain enabled. |
| JDK 25 restricted `System.load` from Gradle's native-platform library | GRADLE/TOOL INTERNAL | The 1.7.10 wrapper JVM and Gradle daemon receive `--enable-native-access=ALL-UNNAMED`; compiler and Minecraft JVM arguments are unchanged. |
| Root/common `JavaPluginConvention` deprecation | OUR BUILD LOGIC | Fixed by configuring source/target compatibility through the `java` extension. |
| Task-time `Project` access in shared version generation, metadata and language actions | OUR BUILD LOGIC | Fixed by capturing paths/target settings and reading detached version state updated by the existing allocator. |
| Duplicate SRG declaration in 1.8.9; 1.12.2 FG3 replaces an explicitly declared SRG config | OUR BUILD LOGIC | Removed redundant `searge()` declarations. Forge's transformers still supply the same SRG mappings; stable MCP 22/39 names remain selected. |
| 1.16.5 IDE resource copy enabled without an IDE plugin | OUR BUILD LOGIC | Applied the Gradle `idea` plugin to this isolated target; `genIntellijRuns` remains available. |
| Legacy Forge MCP ZIP absent from NFRT's artifact manifest | UPSTREAM PLUGIN | Added the exact MCP ZIP to a build-only configuration and supplied it through the pinned plugin's public `addArtifactsToManifest` API. No warning strings are filtered. |
| Unimined remap tasks access `Task.project` during execution | UPSTREAM PLUGIN | Retained; the pinned plugin owns these actions. |
| GTNHGradle's `PropertiesConfiguration.GradleUtils.makePropertiesFrom` calls `Project.getProperties()` | UPSTREAM PLUGIN | Retained; requires an upstream change before Gradle 10. |
| ForgeGradle's download/remapping tasks use task-time `Project` access and `Project.javaexec(Action)` | UPSTREAM PLUGIN | Retained; no supported per-warning suppression was found. |

The Legacy Forge manifest warning was a missing upstream manifest entry, not a corrupt cache: [ModDevGradle 2.0.148](https://plugins.gradle.org/m2/net/neoforged/moddev-gradle/2.0.148/moddev-gradle-2.0.148-sources.jar) constructs the manifest without these MCP ZIPs, while [NeoFormRuntime 2.0.31](https://maven.neoforged.net/releases/net/neoforged/neoform-runtime/2.0.31/neoform-runtime-2.0.31-sources.jar) warns on a miss before using its Maven/cache fallback. The added coordinates are `de.oceanlabs.mcp:mcp_config:1.18.2-20220404.173914@zip` and `de.oceanlabs.mcp:mcp_config:1.20.1-20230612.114412@zip`. They are tooling inputs, not mod runtime dependencies. Keep them aligned with Forge userdev if changing the loader pins. NFRT's verbosity setting does not control this warning, so it is not used to silence it.

The [JDK native-access option](https://docs.oracle.com/en/java/javase/25/docs/specs/man/java.html) permits Gradle's expected native-library loading rather than hiding all JVM warnings. Likewise, the javac change disables only the intentional options category; unchecked/deprecation diagnostics remain reportable. No `-nowarn` or `-Xlint:none` is configured.

Remaining upstream deprecations keep their normal Gradle summaries. Suppressing those summaries by owner would also risk hiding future project warnings, so they stay visible. To expose individual warnings in the root **and all child wrappers**, run:

```powershell
./gradlew.bat buildAll --warning-mode all
```

This is a production build and follows the usual counter rules. For non-production diagnostics, use `testAll --warning-mode all` from the root, or `./gradlew.bat classes --warning-mode all "-Dorg.gradle.deprecation.trace=true"` from an adapter directory. Root launchers also forward `--warning-mode fail`; existing upstream deprecations intentionally cause that mode to fail. Daemon notices, `UP-TO-DATE`, `NO-SOURCE`, `SKIPPED`, mapping/artifact setup, cache reuse and `BUILD SUCCESSFUL` remain visible. No warnings from the audited diagnostic runs remain unclassified; an unfamiliar warning should be investigated rather than added to a filter.

## Repository layout

```text
common/                 Portable Java 8 domain sources, assets and JUnit tests
versions/<minecraft>/   Isolated build, entry point, registries, command/chat API and resources
build-logic/            Shared source/resource, packaging and version transaction conventions
scripts/                JDK selection, wrapper dispatch and aggregate release coordinator
.github/workflows/      Independent matrix builds and eight-target tag releases
.run/                   Portable IntelliJ shortcuts for root Gradle launchers
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

### Windows prerequisites

Install **JDK 8, JDK 17, JDK 21, JDK 25, Python 3.9+ and Git**. [Eclipse Temurin](https://adoptium.net/installation/) is the recommended JDK distribution; other compatible full JDK distributions work too. With Windows Package Manager, installation examples are:

```powershell
winget install EclipseAdoptium.Temurin.8.JDK
winget install EclipseAdoptium.Temurin.17.JDK
winget install EclipseAdoptium.Temurin.21.JDK
winget install EclipseAdoptium.Temurin.25.JDK
winget install Python.Python.3.13
winget install Git.Git
```

In Windows **Environment Variables**, add these user variables pointing to each JDK's installation directory, rather than its `bin` directory. This is an example layout; substitute the paths actually installed on your machine:

```text
JAVA8_HOME  = C:\Program Files\Eclipse Adoptium\jdk-8.0.504.1-hotspot
JAVA17_HOME = C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot
JAVA21_HOME = C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot
JAVA25_HOME = C:\Program Files\Eclipse Adoptium\jdk-25.0.4.101-hotspot
```

Ensure Python and Git are available on `PATH`. After installing Python or changing user environment variables, restart Windows terminals and IntelliJ IDEA so they inherit the new environment. Verify from a new PowerShell terminal:

```powershell
$env:JAVA8_HOME
$env:JAVA17_HOME
$env:JAVA21_HOME
$env:JAVA25_HOME

& "$env:JAVA8_HOME\bin\java.exe" -version
& "$env:JAVA17_HOME\bin\java.exe" -version
& "$env:JAVA21_HOME\bin\java.exe" -version
& "$env:JAVA25_HOME\bin\java.exe" -version

python --version
git --version
```

The project intentionally does not rely on one global `JAVA_HOME`. Its wrappers select the build JVM for each target, and its compiler/game toolchains select the other JDKs. Keep the four variables available rather than manually switching your global Java installation between targets.

### Build JVM, source language and runtime

```text
Gradle JVM != source language level != produced classfile/runtime requirement
```

The build JVM runs Gradle and its plugins. The compiler's language settings determine which syntax is accepted; its target settings determine the classfile/API baseline required by the resulting mod. For example:

- 1.6.4 runs Unimined/Gradle on JDK 21 and compiles Java 8-compatible source and bytecode with `--release 8`; Minecraft runs on Java 8.
- 1.7.10 runs GTNHGradle on JDK 25 and uses Jabel with a JDK 17 compiler to accept Java 17 syntax while producing Java 8-compatible runtime bytecode and APIs.
- 1.18.2 and 1.20.1 compile for and require Java 17 at runtime, although JDK 21 runs their builds.
- 1.21.11 compiles for and requires Java 21 at runtime.

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

## IntelliJ IDEA workflow

### Root project and launcher tasks

Open the repository root as a Gradle project. In **Settings > Build, Execution, Deployment > Build Tools > Gradle**, select the project's **Gradle wrapper** and **JDK 21** as the root Gradle JVM, then refresh the Gradle project. IntelliJ uses this [Gradle JVM setting](https://www.jetbrains.com/help/idea/gradle-settings.html) for import/task execution; the custom shell/batch wrapper's JDK selection does not configure the IDE itself. Keep the Windows variables above available to IntelliJ's child processes.

The root import models common code and its tests only. It also exposes the following tasks under **Tasks > multiversion** in the Gradle tool window:

| Minecraft | Build | Development client | Development server |
|---|---|---|---|
| 1.6.4 | `build1_6_4` | `runClient1_6_4` | `runServer1_6_4` |
| 1.7.10 | `build1_7_10` | `runClient1_7_10` | `runServer1_7_10` |
| 1.8.9 | `build1_8_9` | `runClient1_8_9` | `runServer1_8_9` |
| 1.12.2 | `build1_12_2` | `runClient1_12_2` | `runServer1_12_2` |
| 1.16.5 | `build1_16_5` | `runClient1_16_5` | `runServer1_16_5` |
| 1.18.2 | `build1_18_2` | `runClient1_18_2` | `runServer1_18_2` |
| 1.20.1 | `build1_20_1` | `runClient1_20_1` | `runServer1_20_1` |
| 1.21.11 | `build1_21_11` | `runClient1_21_11` | `runServer1_21_11` |

`buildAll` builds all eight targets using one production number; `testAll` runs common tests and all eight targets' shared tests. Root `:common:test` remains available for a quick common-only test pass. Double-click a task to run it, or use the terminal:

```powershell
./gradlew.bat build1_7_10
./gradlew.bat build1_20_1
./gradlew.bat build1_21_11
./gradlew.bat testAll
./gradlew.bat buildAll
./gradlew.bat runClient1_7_10
./gradlew.bat runServer1_20_1
```

On Unix/macOS, use `./gradlew` in place of `./gradlew.bat`. All launchers need Python 3.9+ (`python` on Windows, `python3` on Unix, or the executable specified by `PYTHON`). Each launcher delegates to the existing Python coordinator, which invokes only the selected target's `.bat` or shell wrapper. No loader plugin is applied to the root, and target Gradle processes retain their own wrappers, daemons and toolchains. The aggregate tasks run `:common:test` as a parent prerequisite and tell the coordinator to skip its usual root test invocation, avoiding a recursive root build and cache-lock contention. Direct script usage still runs common tests normally.

`runClient*`, `runServer*` and `testAll` do not increment `version.properties`. `build*` launchers retain the existing production transaction: one increment per successful single-target build, or one shared increment for a successful `buildAll`. Multiple single-target build launchers consume separate numbers; use `buildAll` for a coordinated set. Root launchers do not allocate versions themselves. Avoid simultaneous production launchers; the existing root lock rejects competing builds.

To inspect a development launcher's native task graph without starting Minecraft, use:

```powershell
./gradlew.bat runClient1_7_10 -PtargetDryRun=true
```

This forwards `--dry-run` to that target's native `runClient` task. Root `--dry-run` alone only lists the parent launcher and never invokes the child wrapper. Neither inspection consumes a production number.

Portable Gradle run configurations under `.run/` provide **Build All Versions**, **Test All Versions**, and build/client shortcuts for **1.7.10**, **1.20.1** and **1.21.11** in IntelliJ's Run menu after import. They use `$PROJECT_DIR$` and the root task names, with no machine-specific JDK paths. To add another shortcut, create a [Gradle run configuration](https://www.jetbrains.com/help/idea/run-debug-gradle.html), select the root project, enter its launcher task name and select **Store as project file**. Keep shared configurations under `.run/`; `.idea/` remains ignored.

### Editing or debugging one adapter

For Minecraft-aware completion, native development/debug configurations, Minecraft sources and loader integration, open/import `versions/1.7.10`, `versions/1.20.1` or another adapter as its own Gradle project, optionally in a separate IntelliJ window. Select that adapter's wrapper and the **Build JDK** from the support table as its IntelliJ Gradle JVM (25 for 1.7.10, 21 for 1.20.1). Its native tooling then supplies the proper classpath and client/server environment. Use the native run configurations when debugging Minecraft; debugging a root process launcher does not attach an IDE debugger to the child game automatically.

This separate import is intentional. A root import provides shared-code editing and convenient launchers; it does not model all eight Minecraft classpaths at once. No permanent version branches or combined loader build are needed.

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
5. Supply only the target-specific resource formats, then add the target to the coordinator, root launcher list and CI matrix. Add portable `.run/` shortcuts where useful. Build, inspect the JAR and run client/dedicated-server checks.

EditorConfig defines four-space Java indentation and two-space JSON/YAML indentation. Preserve loader-specific APIs and keep common independent. Generated IDE data, caches, JAR outputs and logs are ignored. The checked-in official Gradle bootstrap JARs are the small wrapper exception. Existing Forge/FML license and credit files remain as third-party notices; ExampleMod source is MIT licensed.
