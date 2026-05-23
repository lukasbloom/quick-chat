# Quick WA

Lightweight Android app that opens a WhatsApp chat with any phone number, no contact saving required.

## How it works

1. Select a phone number anywhere in Android (browser, messaging app, PDF, email, notes…)
2. Tap **Open in WhatsApp** in the text-selection menu, or use **Share → Quick WA**
3. WhatsApp opens straight on the conversation

No saved contact, no manual cleanup of the number, no country code typing. The app:

- strips formatting (spaces, dashes, parens, dots)
- treats `+…` and `00…` as international (no prefix added)
- otherwise infers the country from network → SIM → device locale
- parses with Google `libphonenumber`
- launches `https://wa.me/<E.164-digits>` via `ACTION_VIEW`

## Permissions

None dangerous. Uses `TelephonyManager.networkCountryIso` / `simCountryIso`, which are normal-permission accessors — no `READ_PHONE_STATE`, no location, no contacts, no SMS, no accessibility service.

## Build

Requires JDK 17+ and the Android SDK (API 34). Open in Android Studio (Hedgehog or newer), or from the command line:

```bash
# First time only: generate the Gradle wrapper jar
gradle wrapper --gradle-version 8.9

# Debug APK
./gradlew :app:assembleDebug

# Install on connected device
./gradlew :app:installDebug
```

The debug APK lands in `app/build/outputs/apk/debug/app-debug.apk`.

## Test

```bash
./gradlew :app:testDebugUnitTest
```

30 host-JVM tests cover the deterministic logic and the wiring:

- `PhoneNumberNormalizerTest` (17) — all PRD examples, extension/vanity/Arabic-Indic edge cases, multiple-number ordering, URL/junk inputs.
- `WhatsAppLauncherTest` (4) — `wa.me` URL builder.
- `CountryResolverTest` (4) — Robolectric: network → SIM → locale priority and unsupported-region whitelist.
- `EntryActivityTest` (5) — Robolectric: end-to-end PROCESS_TEXT / SEND wiring, package-targeted VIEW intent (`com.whatsapp`), garbage handling, `RESULT_CANCELED` for PROCESS_TEXT.

## Architecture

Single-module, four small Kotlin files under `app/src/main/kotlin/com/quickwa/`:

| File | Responsibility |
|---|---|
| `EntryActivity.kt` | Transparent no-history activity; handles `ACTION_PROCESS_TEXT` + `ACTION_SEND` |
| `PhoneNumberNormalizer.kt` | Cleanup + libphonenumber parsing → E.164 digits |
| `CountryResolver.kt` | network → SIM → locale region resolution |
| `WhatsAppLauncher.kt` | Builds `wa.me` URL, launches via `ACTION_VIEW` with `setPackage("com.whatsapp")` → `com.whatsapp.w4b` → generic VIEW → Play Store fallback |

No Compose, no DI framework, no analytics, no network, no database, no background work.

## Minimum SDK

API 23 (Android 6.0). `ACTION_PROCESS_TEXT` requires API 23+.
