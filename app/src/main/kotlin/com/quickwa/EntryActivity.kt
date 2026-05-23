package com.quickwa

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

/**
 * Single entry point. Receives PROCESS_TEXT (text-selection menu) or SEND
 * (share sheet), normalizes the payload, launches WhatsApp, finishes.
 *
 * Theme.NoDisplay + noHistory + excludeFromRecents means the user never sees
 * this activity — they see WhatsApp open, or a Toast on failure.
 */
class EntryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PROCESS_TEXT is a result-bearing contract. We never modify the
        // selected text, so RESULT_CANCELED is the honest answer. Set it once
        // up front; any return path below just calls finish().
        setResult(RESULT_CANCELED)

        val text = extractText(intent)
        if (text.isNullOrBlank()) { fail(R.string.error_no_number); return }

        val region = CountryResolver(this).resolve()
        val digits = PhoneNumberNormalizer().normalize(text, region)
        if (digits == null) { fail(R.string.error_no_number); return }

        val launched = WhatsAppLauncher(this).launch(digits)
        if (!launched) { fail(R.string.error_whatsapp_missing); return }
        finish()
    }

    private fun extractText(i: Intent?): String? = when (i?.action) {
        Intent.ACTION_PROCESS_TEXT -> i.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        Intent.ACTION_SEND -> i.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
        else -> null
    }

    private fun fail(resId: Int) {
        // Toast must be bound to the application context, not the activity —
        // Theme.NoDisplay activities have already lost their window token by
        // the time the toast actually renders, which silently drops it on
        // some OEM builds.
        Toast.makeText(applicationContext, resId, Toast.LENGTH_SHORT).show()
        finish()
    }
}
