# JOYVERSE 🪐
> *"Discover More. Create More. Connect Through Hobbies."*

[![Android CI & Build](https://github.com/your-username/joyverse/actions/workflows/android-ci.yml/badge.svg)](https://github.com/your-username/joyverse/actions/workflows/android-ci.yml)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg)](https://developer.android.com/jetpack/compose)

**JOYVERSE** is a personal activity universe connected to a worldwide hobby discovery community. It combines personal activity tracking, intelligent free-time recommendations, global cultural exploration, and interest-first spaces—prioritizing genuine curiosity, personal development, and pseudonymous privacy over conventional popularity or endless scrolling.

---

## 🚀 Key Features

1. **🌌 Personal Activity Universe**
   - Offline-first personal activity & task management backed by Room Database.
   - Dual-layer synchronization with Google Cloud Firestore.
   - Categorized scheduling (Study, Coding, Languages, Gaming, Creativity, Exploration).
   - Time-based dashboard greetings, activity streaks 🔥, and XP progress bars.

2. **⏱️ Intelligent FreeTime Corner**
   - Immediate activity suggestions tailored to your exact available time (5m, 10m, 15m, 30m, 45m, 60m).
   - Contextual mood (Curious, Relaxed, Creative, Motivated, Adventurous) & energy level filtering.
   - Instant "Add to Activities" action with transparent rationale explanations.

3. **🌍 WorldVerse Global Explorer**
   - Country explorer featuring verified geography, capitals, currencies, and cultural heritage.
   - Daily language vocabulary lessons with pronunciations and nuances (e.g. *Komorebi*, *Gluggaveður*, *Harambee*).
   - Random discovery generator for global curiosity.

4. **👥 Hobby-First Spaces**
   - Interest-first directory connecting people through shared crafts rather than follower graphs.
   - Spaces for Android, Web Building, Astrophotography, Japanese Culture, and Creative Experiments.

5. **🛡️ Pseudonymous Identity System**
   - Privacy-by-design: No real names or emails exposed publicly.
   - 8 custom cosmic avatar presets with customizable explorer handles and bios.
   - Zero private authentication data leaks.

6. **⚡ Gamified Progression & Daily Quests**
   - Interactive daily quests awarding experience points (XP).
   - Dynamic leveling system calculated through verified transactions.

---

## 🛠️ Architecture & Tech Stack

- **UI & Layout:** Jetpack Compose, Material 3, Edge-to-Edge full bleed, Responsive WindowInsets
- **Language:** Kotlin 2.0+ & Coroutines / StateFlow
- **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
- **Local Persistence:** Room Database (`JoyverseDatabase`) with SQLite DAO
- **Cloud Backend:** Firebase Cloud Firestore (Enterprise Edition)
- **Authentication:** Google Sign-In via Jetpack `CredentialManager` (`GetSignInWithGoogleOption`)
- **Security:** Zero-trust Firestore Security Rules (`firestore.rules`) with verified schema bounds

---

## 📦 Project Structure

```
JOYVERSE/
├── .github/
│   └── workflows/
│       ├── android-ci.yml              # CI: Unit tests, lint, and Debug APK build
│       └── firestore-rules-ci.yml       # CI: Security rules emulator unit tests
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt      # Auth Gate & Root Navigation
│   │   │   │   ├── core/               # Domain Models, Room DB, Repositories
│   │   │   │   ├── feature/            # Auth, Home, Activities, FreeTime, Explore, Profile
│   │   │   │   ├── navigation/         # 5-Destination Navigation Bar
│   │   │   │   └── ui/theme/           # Cosmic M3 Theme & Design Tokens
│   │   │   └── res/                    # Adaptive Launcher Icons & Values
│   │   └── test/                       # Robolectric & JVM Unit Tests
├── firebase-blueprint.json             # Schema Intermediate Representation
├── firestore.rules                     # Zero-Trust Firestore Security Rules
├── firestore.test.js                   # JavaScript Security Rules Unit Tests
└── build.gradle.kts                    # Root Build Configuration
```

---

## ⚙️ Building & Running Locally

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17 (or newer)
- Android SDK Platform 36

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/joyverse.git
   cd joyverse
   ```

2. **Open in Android Studio:**
   - Select **File > Open...** and choose the `joyverse` folder.
   - Wait for Gradle sync to complete.

3. **Run Unit Tests:**
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Build the Debug APK:**
   ```bash
   ./gradlew assembleDebug
   ```
   The generated APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🤖 GitHub Actions CI/CD Workflows

This repository includes automated GitHub Actions:

- **`android-ci.yml`**:
  - Automatically triggers on every `push` and `pull_request` to `main` / `master`.
  - Runs all JUnit & Robolectric integration tests.
  - Compiles the Android project.
  - Builds and uploads the Debug APK as an downloadable artifact on GitHub.
  
- **`firestore-rules-ci.yml`**:
  - Automatically runs when `firestore.rules` is updated.
  - Spins up the Firestore and Auth emulators.
  - Validates all 5 rule test scenarios using `@firebase/rules-unit-testing`.

---

## 🚀 How to Publish to Your GitHub

1. **Create a new repository on GitHub:**
   - Go to [GitHub New Repository](https://github.com/new).
   - Name it `joyverse` (or your preferred name).
   - Leave it empty (do not initialize with README, .gitignore, or license as they are already in the project).

2. **Push from your terminal:**
   ```bash
   git init
   git add .
   git commit -m "feat: initial JOYVERSE release with GitHub Actions CI/CD"
   git branch -M main
   git remote add origin https://github.com/<your-username>/joyverse.git
   git push -u origin main
   ```

3. **Check GitHub Actions:**
   - Navigate to the **Actions** tab in your GitHub repository.
   - Watch the `Android CI & Build` workflow run unit tests and automatically generate your APK!
