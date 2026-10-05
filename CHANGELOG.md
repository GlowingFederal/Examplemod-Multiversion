# Changelog

2026-10-05 13:13 — Establish the multi-version ExampleMod architecture

- Replace the single 1.7.10 template with eight independently buildable Forge/NeoForge adapters and portable shared domain sources, tests and assets.
- Select maintained tooling per Minecraft generation, including GTNHGradle/RetroFuturaGradle with Jabel for standalone Java 8-compatible Forge 1.7.10 output.
- Add pinned wrappers, automatic build-JDK selection, shared metadata generation and transactional root production build numbering.
- Add coordinated build/test commands, independent CI builds and an eight-target release workflow using one version per release.
