# Play Console listing copy — QuickChat

Paste the relevant sections into the corresponding Play Console fields. Fields are listed in the order Play Console asks for them.

---

## App name *(max 30 characters)*

```
QuickChat
```
*(9 characters — leaves room if you later want to tag it, e.g. "QuickChat: WA opener".)*

## Short description *(max 80 characters)*

```
Open a WhatsApp chat with any selected phone number. No contacts saved.
```
*(72 characters.)*

## Full description *(max 4000 characters)*

```
QuickChat opens a WhatsApp conversation with any phone number — selected from any app — without saving a contact and without typing the country code.

HOW IT WORKS
1. Select a phone number anywhere: a web page, an email, a PDF, a note, an SMS.
2. Tap "Open in WhatsApp" in your phone's text-selection menu.
3. WhatsApp opens straight to that conversation.

That's it. No contact saved, no extra steps, no copy-paste, no manual cleanup of spaces and dashes.

WHY YOU'LL LIKE IT
• Strips spaces, dashes, parentheses, dots automatically.
• Recognises numbers with "+" and "00" international prefixes.
• Infers the country code from your network and SIM when the number is in local format.
• Works with the standard text-selection menu — no overlays, no floating buttons, no accessibility services.
• Also available in the system Share menu as a fallback.

PRIVACY FIRST
• Zero data collected, zero data transmitted, zero data stored.
• No analytics. No advertising. No crash reporting.
• No internet permission. The app cannot connect to any server even if it tried.
• Uses your phone's own region settings to format local numbers, with no need for location, contacts, SMS, or call-log permissions.
• Open-source under the hood: powered by Google's libphonenumber for accurate parsing.

REQUIREMENTS
• Android 6.0 (Marshmallow) or higher.
• WhatsApp installed on the device.

CONTACT
Questions, bug reports, suggestions: lucas.ariel.barros+quickchat@gmail.com

QuickChat is an independent third-party utility. It is not affiliated with, endorsed by, or sponsored by WhatsApp LLC or Meta Platforms, Inc. "WhatsApp" is a trademark of WhatsApp LLC.
```

## App category

**Communication**

(Alternative: Tools — but Communication is the better fit because the app's job is to open a conversation.)

## Tags *(up to 5)*

```
Phone, Messaging, Productivity, Utilities, Quick Actions
```

## Contact details

- **Email:** `lucas.ariel.barros+quickchat@gmail.com`
- **Phone:** *(leave blank unless you want users to call you)*
- **Website:** `https://lukasbloom.github.io/quick-chat/` *(GitHub Pages root)*

## Privacy policy URL

```
https://lukasbloom.github.io/quick-chat/privacy-policy.html
```

---

# Data safety form

Play Console > App content > Data safety. Answer each question as follows.

## "Does your app collect or share any of the required user data types?"

> **No**

(The form will then collapse most subsequent sections.)

## "Is all of the user data collected by your app encrypted in transit?"

> **N/A — no data collected**

(Some Play Console flows still surface this question even after a "No" above. Answer accordingly.)

## "Do you provide a way for users to request that their data be deleted?"

> **N/A — no data collected**

## Specific data type questions (if surfaced)

For each category Play Console lists (Location, Personal info, Financial info, Health and fitness, Messages, Photos and videos, Audio files, Files and docs, Calendar, Contacts, App activity, Web browsing, App info and performance, Device or other IDs):

> **Not collected**

---

# Content rating questionnaire

Open Play Console > App content > Content ratings. The IARC questionnaire is short. For QuickChat, answer:

- **Category of app:** Reference, News, or Educational *(or "Other" if asked — Communication is not always a top-level IARC choice)*
- **Violence:** None
- **Sexuality:** None
- **Language:** None
- **Controlled substances:** None
- **Gambling:** None
- **User-generated content:** No
- **User interaction:** No (the app does not let users contact each other — WhatsApp does, but the app itself is a launcher)
- **Shares location:** No
- **Digital purchases:** No

Expected rating: **Everyone / 3+**.

---

# Target audience and content

- **Target age range:** 13 and older *(or 18 and older if you prefer — there is no reason for a child to need this utility, but the content is harmless).*
- **Appeals to children?:** No.
- **Ads:** No.

---

# App access

- **Login required?:** No.
- **Special access required to use the app?:** No.

Add the following instructions in the "Instructions" text box so reviewers can verify the app:

```
QuickChat has no login or onboarding. To verify:
1. Install the app.
2. Open the Notes app (or any text-containing app).
3. Type or paste a phone number such as +34 612 33 44 55.
4. Select the number with a long-press.
5. In the floating text-selection menu, tap the three-dot overflow.
6. Tap "Open in WhatsApp".
7. WhatsApp will open directly to a chat with that number.

The app also appears in the system Share menu as an alternative.
```

---

# Release notes for first release

```
Initial release.

• Select any phone number → tap "Open in WhatsApp" → WhatsApp opens to that chat.
• Works from text-selection menu and from the system Share menu.
• No contact saving, no permissions beyond what Android grants automatically, no internet access.
```

---

# Pre-launch checklist (do these in order)

1. **Enable GitHub Pages** for the repo at https://github.com/lukasbloom/quick-chat/settings/pages → branch master, folder /docs → Save.
   Verify the policy URL responds: `curl -sI https://lukasbloom.github.io/quick-chat/privacy-policy.html | head -1`
2. **Create the Play Console developer account** at https://play.google.com/console ($25 one-time, ~24h verification).
3. **Create a new app** in Play Console with:
   - App name: QuickChat
   - Default language: English (United States)
   - Free
   - You agree to Play Developer Policies
4. **Set up the store listing** with the copy in this file.
5. **Upload the AAB**: `app/build/outputs/bundle/release/app-release.aab`.
6. **Fill in data safety / content rating / target audience / app access** per this file.
7. **Submit for review.** First review typically takes 1–3 days.
