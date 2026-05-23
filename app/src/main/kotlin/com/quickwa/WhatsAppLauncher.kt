package com.quickwa

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Opens a WhatsApp conversation for an E.164 digits-only number via the
 * `https://wa.me/<digits>` deep link.
 *
 * The PRD goal is "WhatsApp opens *directly*" — relying on the system URL
 * chooser breaks that promise on devices where a browser is the default
 * https handler. So we walk a targeted-package chain:
 *
 *   1. com.whatsapp        (consumer app)
 *   2. com.whatsapp.w4b    (business app)
 *   3. ACTION_VIEW with no package set (last-resort: browser, etc.)
 *   4. Play Store listing for WhatsApp (fallback when nothing handles step 3)
 *
 * Returns true when any of steps 1–3 launched something, false when we fell
 * through to the Play Store fallback.
 */
class WhatsAppLauncher(private val context: Context) {

    fun launch(e164DigitsOnly: String): Boolean {
        val url = buildUrl(e164DigitsOnly) ?: return false
        val uri = Uri.parse(url)

        for (pkg in WHATSAPP_PACKAGES) {
            if (tryStart(Intent(Intent.ACTION_VIEW, uri).setPackage(pkg))) return true
        }
        if (tryStart(Intent(Intent.ACTION_VIEW, uri))) return true

        tryStart(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL)))
        return false
    }

    private fun tryStart(intent: Intent): Boolean {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    companion object {
        private val WHATSAPP_PACKAGES = listOf("com.whatsapp", "com.whatsapp.w4b")
        private const val PLAY_STORE_URL =
            "https://play.google.com/store/apps/details?id=com.whatsapp"

        /**
         * Builds the wa.me URL. Input must already be E.164 digits-only — the
         * normalizer is the source of truth for "is this a valid number". This
         * function is the last gate before we hand untrusted-shaped data to
         * Intent.parse(), so it just ensures we never construct a URL that
         * isn't `https://wa.me/<digits>`.
         */
        fun buildUrl(digits: String?): String? {
            if (digits.isNullOrBlank()) return null
            if (!digits.all { it.isDigit() }) return null
            return "https://wa.me/$digits"
        }
    }
}
