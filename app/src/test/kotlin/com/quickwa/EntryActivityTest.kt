package com.quickwa

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * Host-side integration test for [EntryActivity]. Locks in the wiring between
 * the PROCESS_TEXT / SEND intents, the normalizer, the launcher, and the
 * outbound wa.me VIEW intent.
 *
 * Robolectric runs on the host JVM — no emulator required.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class EntryActivityTest {

    private fun launchWith(action: String, extraKey: String, text: String): Activity {
        val intent = Intent(action).apply {
            setClassName(ApplicationProvider.getApplicationContext(), EntryActivity::class.java.name)
            putExtra(extraKey, text)
        }
        return Robolectric.buildActivity(EntryActivity::class.java, intent).create().get()
    }

    private fun Activity.nextStartedView(): Intent? {
        val started = shadowOf(application).peekNextStartedActivity() ?: return null
        return started.takeIf { it.action == Intent.ACTION_VIEW }
            ?: shadowOf(application).nextStartedActivity
    }

    @Test fun `PROCESS_TEXT with valid international number fires wa-me VIEW`() {
        val activity = launchWith(
            Intent.ACTION_PROCESS_TEXT,
            Intent.EXTRA_PROCESS_TEXT,
            "+34 612 33 44 55"
        )
        val launched = shadowOf(activity.application).nextStartedActivity
        assertNotNull("Expected a VIEW intent to be started", launched)
        assertEquals(Intent.ACTION_VIEW, launched.action)
        assertEquals("https://wa.me/34612334455", launched.dataString)
        // First attempt must target WhatsApp explicitly (the whole point of
        // the recent refactor).
        assertEquals("com.whatsapp", launched.`package`)
        assertTrue(activity.isFinishing)
    }

    @Test fun `SEND with valid international number fires wa-me VIEW`() {
        val activity = launchWith(
            Intent.ACTION_SEND,
            Intent.EXTRA_TEXT,
            "Call me at +34612334455"
        )
        val launched = shadowOf(activity.application).nextStartedActivity
        assertNotNull(launched)
        assertEquals("https://wa.me/34612334455", launched.dataString)
        assertEquals("com.whatsapp", launched.`package`)
    }

    @Test fun `garbage input launches nothing and finishes`() {
        val activity = launchWith(
            Intent.ACTION_PROCESS_TEXT,
            Intent.EXTRA_PROCESS_TEXT,
            "hello world no number"
        )
        assertNull(
            "EntryActivity must not start any activity for unparseable input",
            shadowOf(activity.application).nextStartedActivity
        )
        assertTrue(activity.isFinishing)
    }

    @Test fun `unknown action finishes without launch`() {
        val intent = Intent("com.example.UNKNOWN").apply {
            setClassName(ApplicationProvider.getApplicationContext(), EntryActivity::class.java.name)
            putExtra(Intent.EXTRA_TEXT, "+34 612 33 44 55")
        }
        val activity = Robolectric.buildActivity(EntryActivity::class.java, intent).create().get()
        assertNull(shadowOf(activity.application).nextStartedActivity)
        assertTrue(activity.isFinishing)
    }

    @Test fun `result code is CANCELED for PROCESS_TEXT (we never modify the text)`() {
        val activity = launchWith(
            Intent.ACTION_PROCESS_TEXT,
            Intent.EXTRA_PROCESS_TEXT,
            "+34 612 33 44 55"
        )
        // shadowOf().resultCode exposes what setResult() was called with.
        assertEquals(Activity.RESULT_CANCELED, shadowOf(activity).resultCode)
    }
}
