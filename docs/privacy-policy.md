---
layout: default
title: Privacy Policy — QuickChat
---

# Privacy Policy — QuickChat

**Effective date:** 2026-05-23
**Developer:** Lucas Barros (contact: lucas.ariel.barros@gmail.com)
**App package:** `io.github.lukasbloom.quickchat`

## TL;DR

QuickChat collects nothing, transmits nothing, stores nothing.

The app is a local intent shim that turns a selected phone number into a `https://wa.me/...` link and hands it to WhatsApp. It runs entirely on your device. There is no backend server, no analytics, no crash reporting, no advertising SDKs.

## Data we collect

**None.**

When you select a phone number and tap "Open in WhatsApp":

- QuickChat reads the selected text *only* for the duration of the launch (a few milliseconds), normalises it locally, and immediately hands the resulting `wa.me` link to the WhatsApp app.
- The text is never written to disk, never sent over the network, never logged.
- No identifiers are generated or stored. No account is required.

## Data WhatsApp may collect

Once you tap the "Open in WhatsApp" action, QuickChat's job is done — the rest of the conversation happens inside WhatsApp. WhatsApp's own data practices are governed by WhatsApp's privacy policy, not this one. See [whatsapp.com/legal/privacy-policy](https://www.whatsapp.com/legal/privacy-policy).

## Permissions

QuickChat declares no dangerous Android permissions. It uses:

- **`ACTION_PROCESS_TEXT` / `ACTION_SEND`** intent filters — these are how the app appears in your device's text-selection menu and share sheet. No permission needed.
- **`TelephonyManager.networkCountryIso` and `simCountryIso`** — read at the moment a number is processed to determine which country dialling code to apply when the selected number is local-format. These accessors are "normal" permissions on Android; they do not require user grants, do not reveal your phone number, and do not log anything.

QuickChat does **not** request: contacts, SMS, call logs, location, microphone, camera, storage, accessibility services, foreground service, or internet.

## Third-party libraries

- **[libphonenumber](https://github.com/google/libphonenumber)** — Google's open-source phone number parsing library. Runs entirely on-device; makes no network calls.

No advertising SDKs, no analytics SDKs, no crash reporting SDKs are bundled.

## Children

The app is not directed at children under 13 and does not knowingly collect data from any user, including children.

## Changes to this policy

If this policy changes, the updated version will be published at this URL and the "Effective date" above will be revised. Material changes will be announced in the GitHub repository's release notes.

## Contact

Questions about this policy: **lucas.ariel.barros@gmail.com**
