package com.quickwa

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WhatsAppLauncherTest {

    @Test fun `builds wa-me url from digits`() {
        assertEquals("https://wa.me/34612334455", WhatsAppLauncher.buildUrl("34612334455"))
    }

    @Test fun `rejects non-digit input`() {
        assertNull(WhatsAppLauncher.buildUrl("+34612334455"))
        assertNull(WhatsAppLauncher.buildUrl("34 612 33 44 55"))
        assertNull(WhatsAppLauncher.buildUrl("abc"))
    }

    @Test fun `rejects null and blank`() {
        assertNull(WhatsAppLauncher.buildUrl(null))
        assertNull(WhatsAppLauncher.buildUrl(""))
        assertNull(WhatsAppLauncher.buildUrl("   "))
    }

    @Test fun `accepts arbitrarily long digit string`() {
        // libphonenumber validates length upstream; buildUrl is the last gate
        // and only enforces "digits only", not length.
        assertEquals("https://wa.me/1", WhatsAppLauncher.buildUrl("1"))
    }
}
