# habitstreaker

An Android app for daily task tracking with Duolingo-style streaks and reminder notifications.

## Features
- Add customizable daily tasks.
- Keep streak counts per task.
- Receive daily reminder notifications to avoid streak loss.
- Dark, nerdy neon-inspired UI built with Jetpack Compose.
- Quick reminder-time adjustment per task.

## Tech
- Kotlin + Jetpack Compose
- DataStore for local persistence
- AlarmManager + BroadcastReceiver for notifications

## Prerequisites
- Android Studio (Koala or newer recommended)
- Android SDK Platform 34
- Android SDK Build-Tools 34.x (or the latest installed by Android Studio)
- JDK 17

## Run in Android Studio (recommended)
1. Open Android Studio.
2. Select **Open** and choose this project folder (`Explore_AI_Tools`).
3. Let Gradle sync finish.
4. Create or start an emulator (API 26+), or connect a physical device with USB debugging.
5. Press **Run** ▶ and select the `app` configuration.

## Run from terminal
If you have Android SDK configured locally:

```bash
# from project root
./gradlew assembleDebug
./gradlew installDebug
```

Then launch the app from your device/emulator app drawer as **habitstreaker**.

## If you see the `25.0.1` build error
That usually means your local Android SDK/Build-Tools are incomplete or not configured for this environment.

- In Android Studio: **Settings > Android SDK**
  - Install **Android SDK Platform 34**
  - Install **Android SDK Build-Tools** (latest)
- Ensure environment variables are set when building from terminal:
  - `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) points to your SDK path
  - `PATH` includes `$ANDROID_HOME/platform-tools` and `$ANDROID_HOME/cmdline-tools/latest/bin`

