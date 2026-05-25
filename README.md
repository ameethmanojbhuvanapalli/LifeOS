# LifeOS (Personal Tracker App)

A modular, offline-first **personal tracker** app built with **Kotlin**, **MVVM + Clean Architecture**, **Room**, **Hilt**, **Jetpack Compose**, and **Firebase Firestore sync**.

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM + Clean Architecture
- **Local DB:** Room (SQLite)
- **Cloud Sync:** Firebase Firestore
- **DI:** Hilt (Dagger)
- **Navigation:** Jetpack Navigation Component
- **UI:** Jetpack Compose

## Package
Base package: `com.lifeos`

## Module Layout
The scaffold follows the LLD layers:

- `core/` — base classes, utils, constants, navigation helpers, Result wrapper
- `di/` — Hilt modules
- `data/` — Room + Firestore + repositories + mappers
- `domain/` — models + use cases + repository contracts
- `presentation/` — Compose UI + ViewModels + UI state

## Getting Started
1. Open in Android Studio (Hedgehog+ recommended).
2. Add your `google-services.json` under `app/`.
3. Run `:app`.

## Roadmap
- Phase 1: Foundation (Hilt, Room, navigation)
- Phase 2: Core modules (Todo, Finance, Dashboard)
- Phase 3: Advanced modules (Tracker/Habits, Analytics)
- Phase 4: Sync & Polish (Firestore sync, conflict resolution, backups)

## License
MIT
