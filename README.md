$This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,

 Key Features
 ------------
 - Create groups and add shared expenses in multiple currencies
 - Automatic fair-split suggestions (equal, weighted, percentage)
 - Real-time balance calculation and settlement guidance
 - Personal ledger for income/expenses and budgeting
 - Offline-first data model with seamless sync (when enabled)
 - Consistent UI with Compose Multiplatform

 Architecture
 ------------
 Clean, testable, and scalable patterns:
 - Clean Architecture with clear separation of concerns (Presentation, Domain, Data)
 - MVVM for presentation logic with unidirectional data flow
 - Repository pattern for data access and caching
 - Use Cases (Interactors) encapsulating business rules
 - Coroutines and Flow for asynchronous and reactive streams
 - Expect/Actual declarations to bridge platform-specific implementations

 Tech Stack
 ----------
- Kotlin Multiplatform Mobile (Android, iOS), plus Desktop and Web (Wasm) targets
- Compose Multiplatform for shared UI
- Coroutines + Flow for concurrency and state
- Ktor (planned) for networking and sync
- SQLDelight (planned) for type-safe local storage
- Dependency Injection: Koin or Hilt/Dagger on Android (final choice TBD)
- Kotlin Serialization for models
- Testing: Kotlin Test/JUnit, Turbine for Flow testing, MockK (where applicable)

Project Structure
-----------------
- composeApp/src/commonMain — shared domain, data, and UI components
- composeApp/src/androidMain — Android-specific code (permissions, DI, storage, etc.)
- composeApp/src/iosMain — iOS-specific code (keychain, storage, etc.)
- composeApp/src/jvmMain — Desktop-specific code
- iosApp/ — native iOS entry point and integration

Getting Started
---------------
Prerequisites
- Android Studio (Ladybug+ recommended) with KMM/Compose Multiplatform support
- Xcode 15+ for iOS build/run
- JDK 17+
- Kotlin 2.x compatible toolchain (managed by Gradle)

Clone and Build
1) Clone this repository
2) Sync Gradle in Android Studio
3) Build the shared module and targets

Run Targets
- Android: Run the Android application from Android Studio
- iOS: Open the iOS scheme/run from Xcode or Android Studio (KMM)
- Desktop (JVM): Run the desktop target via Gradle task
- Web (Wasm): `:composeApp:wasmJsBrowserDevelopmentRun`

Scripts & Gradle Tasks (examples)
- Run web dev: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Run desktop: `./gradlew :composeApp:run`
- Run tests: `./gradlew test`

Testing
-------
- Unit tests for domain/use-cases and repositories in common code
- UI state tests for view models using Turbine/CoroutineTest
- Platform-specific tests where needed

Code Style & Quality
--------------------
- Kotlin official code style
- Detekt/Ktlint (recommended)
- Comprehensive CI checks (build, test, lint) — to be added

Roadmap
-------
- [ ] Implement core bill-splitting engine (equal/weighted/percentage)
- [ ] Add SQLDelight persistence and migrations
- [ ] Enable Ktor sync with conflict resolution
- [ ] Multi-currency support with historical FX rates
- [ ] Secure storage for sensitive data
- [ ] Analytics & crash reporting (privacy-first)
- [ ] Polish UI/UX across platforms

License
-------
This project is licensed under the MIT License — see the LICENSE file for details.