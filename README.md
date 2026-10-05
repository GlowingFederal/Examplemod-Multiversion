# 1.7-Forge-Mod-Template
A clean, modernized development template for **Minecraft Forge 1.7.10** modding — using [anatawa12’s ForgeGradle fork](https://github.com/anatawa12/ForgeGradle) for compatibility with current JDKs and Gradle versions.

This template provides a minimal, functional starting point for anyone building or maintaining 1.7.10 mods in 2025 and beyond.

---

## 📦 Features
- ✅ Pre-configured **ForgeGradle 1.2 (anatawa12 fork)**  
- ✅ Compatible with modern **Java 8 / Gradle 8+** environments  
- ✅ Includes proper `mcmod.info` expansion for metadata  
- ✅ Clean and minimal file structure  
- ✅ `gradlew` wrapper for out-of-the-box builds  
- ✅ Safe resource processing (handles `_at.cfg` renames automatically)

---

## 🛠 Requirements
| Dependency | Recommended Version |
|-------------|--------------------|
| Java | 8 (do **not** use Java 9+) |
| Gradle | Included via `gradlew` |
| Minecraft Forge | `1.7.10-10.13.4.1614` |
| JDK Compatibility | Works on modern systems with proper Gradle wrapper |


# ⚙️ Setup Instructions

### 1. Clone and enter the project
```bash
git clone https://github.com/GlowingFederal/1.7-Forge-Mod-Template.git
cd 1.7-Forge-Mod-Template
```

### 2. Initialize the Forge workspace
Run the following command to decompile and set up the 1.7.10 development environment:
```bash
gradlew setupDecompWorkspace
```

### 3. Generate IDE project files
For Eclipse:
```bash
gradlew eclipse
```
For IntelliJ IDEA:
```bash
gradlew idea
```

### 4. Open the project in your IDE
- **Eclipse:** Import as *Existing Gradle Project*  
- **IntelliJ:** Open the folder directly and let Gradle sync

### 5. Build the mod
Once you’ve edited the source, compile it with:
```bash
gradlew build
```

Your compiled `.jar` will appear under:
```
build/libs/
```

### 6. Run the mod in development
You can test it directly by running:
```bash
gradlew runClient
```
and for server testing:
```bash
gradlew runServer
```

---

**Note:**  
Use `gradlew` on Linux/macOS and `gradlew.bat` on Windows.  
Stick to **Java 8** — ForgeGradle 1.2 doesn’t play well with newer JDKs.
