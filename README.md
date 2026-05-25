# LifeOS (Personal Tracker App)

A modular, offline-first **personal tracker** app built with **Kotlin**, **MVVM + Clean Architecture**, **Room**, **Hilt**, and optional **cloud sync** (Firebase Firestore / custom backend).

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture
- **Local DB:** Room (SQLite)
- **Cloud Sync:** Firebase Firestore / Custom Backend (optional)
- **DI:** Hilt (Dagger)
- **Navigation:** Jetpack Navigation Component
- **UI:** Jetpack Compose / XML Views (configurable)

## Folder Structure (target)
This repository is scaffolded to match the Low-Level Design (LLD) structure:

```
app/src/main/java/com/yourapp/lifeos/
  core/
  di/
  data/
  domain/
  presentation/
```

> Note: The base package is set to `com.yourapp.lifeos` as a placeholder. You can rename it later.

## Getting Started
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration.

## Roadmap
- Phase 1: Foundation (Hilt, Room, navigation)
- Phase 2: Core modules (Todo, Finance, Dashboard)
- Phase 3: Advanced modules (Tracker/Habits, Analytics)
- Phase 4: Sync & Polish (Firestore/custom backend)

## License
MIT
