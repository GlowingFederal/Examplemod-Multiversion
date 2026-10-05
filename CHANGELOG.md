# Changelog

2026-10-05 13:13 — Establish the multi-version ExampleMod architecture

- Replace the single 1.7.10 template with eight independently buildable Forge/NeoForge adapters and portable shared domain sources, tests and assets.
- Select maintained tooling per Minecraft generation, including GTNHGradle/RetroFuturaGradle with Jabel for standalone Java 8-compatible Forge 1.7.10 output.
- Add pinned wrappers, automatic build-JDK selection, shared metadata generation and transactional root production build numbering.
- Add coordinated build/test commands, independent CI builds and an eight-target release workflow using one version per release.

2026-10-05 14:18 — Add Windows setup and IntelliJ multiversion launchers

- Document Windows JDK/Python/Git setup, per-JDK environment variables and the distinction between build JVM, source language and mod runtime.
- Expose per-target build/client/server tasks and aggregate build/test tasks in the root Gradle multiversion group, delegating to isolated target wrappers through the existing coordinator.
- Run common tests in the parent aggregate build to avoid recursively launching the root project; preserve the existing production allocator and non-production numbering behavior.
- Add portable IntelliJ Gradle shortcuts and document separate adapter imports for native sources, classpaths and debugging.

2026-10-05 14:54 — Reduce build warning noise without hiding failures

- Suppress only obsolete javac options on the three Unimined Java 8 targets and grant native-library access only to the 1.7.10 Gradle wrapper/daemon JVMs.
- Replace the common project's deprecated Java convention API and remove task-time Project access from shared version/resource actions without changing the production allocator.
- Supply Legacy Forge's exact MCP ZIPs through ModDevGradle's artifact-manifest API, enable the 1.16.5 idea plugin and remove redundant 1.8.9/1.12.2 SRG mapping declarations.
- Forward full/failing warning diagnostics through root launchers and document retained upstream deprecations; keep normal compiler warnings, Gradle diagnostics and build failures visible.
