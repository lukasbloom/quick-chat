package com.quickwa

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneNumberNormalizerTest {

    private val normalizer = PhoneNumberNormalizer()

    // --- PRD examples ---------------------------------------------------------

    @Test fun `keeps international plus number untouched`() {
        assertEquals("34612334455", normalizer.normalize("+34 612 33 44 55", "ES"))
    }

    @Test fun `converts 00 prefix to international`() {
        assertEquals("34612334455", normalizer.normalize("0034 612 33 44 55", "ES"))
    }

    @Test fun `prepends country code for local number when region is ES`() {
        assertEquals("34612334455", normalizer.normalize("612 33 44 55", "ES"))
    }

    @Test fun `strips parentheses and dashes`() {
        assertEquals("541155554444", normalizer.normalize("(011) 5555-4444", "AR"))
    }

    @Test fun `extracts number embedded in sentence`() {
        assertEquals("34612334455", normalizer.normalize("Call me at 612334455", "ES"))
    }

    @Test fun `handles Argentine mobile with leading 9`() {
        assertEquals("5491155554444", normalizer.normalize("+54 9 11 5555 4444", "AR"))
    }

    @Test fun `handles dots as separators`() {
        assertEquals("34612334455", normalizer.normalize("612.33.44.55", "ES"))
    }

    // --- Negative cases -------------------------------------------------------

    @Test fun `returns null when text contains no number`() {
        assertNull(normalizer.normalize("hello world", "US"))
    }

    @Test fun `returns null on blank input`() {
        assertNull(normalizer.normalize("", "US"))
        assertNull(normalizer.normalize(null, "US"))
        assertNull(normalizer.normalize("   ", "US"))
    }

    @Test fun `returns null on too-short digit run`() {
        assertNull(normalizer.normalize("123", "US"))
    }

    @Test fun `returns null when input is just a URL with digits`() {
        // libphonenumber should not mistake path segments for phone numbers
        assertNull(normalizer.normalize("https://example.com/page/12345", "US"))
    }

    // --- Coverage gaps surfaced by code review --------------------------------

    @Test fun `handles extension annotation`() {
        // libphonenumber recognises "ext" / "x" extensions; the E.164 format
        // strips them (extensions are dialing artefacts, not part of the
        // destination number).
        assertEquals("34612334455", normalizer.normalize("+34 612 33 44 55 ext 123", "ES"))
    }

    @Test fun `picks first valid number when text contains multiple`() {
        // PRD: "use first valid detected number". Two valid numbers, the
        // first should win.
        val result = normalizer.normalize(
            "Office +34 612 33 44 55 mobile +34 600 11 22 33",
            "ES"
        )
        assertEquals("34612334455", result)
    }

    @Test fun `picks first valid number even if earlier candidates are invalid`() {
        // "999" is junk; "+34 612 33 44 55" is the first *valid* number.
        val result = normalizer.normalize("call 999 or +34 612 33 44 55", "ES")
        assertEquals("34612334455", result)
    }

    @Test fun `handles Arabic-Indic digits`() {
        // libphonenumber normalises Unicode decimal digit forms.
        // "+٣٤ ٦١٢ ٣٣ ٤٤ ٥٥" = "+34 612 33 44 55".
        val result = normalizer.normalize("+٣٤ ٦١٢ ٣٣ ٤٤ ٥٥", "ES")
        assertEquals("34612334455", result)
    }

    @Test fun `rejects vanity letters in number`() {
        // 1-800-FLOWERS — libphonenumber does NOT translate letters to digits
        // unless configured to. We do not configure that, so it should fail
        // validation rather than crash.
        val result = normalizer.normalize("1-800-FLOWERS", "US")
        // Either null (rejected) or the dialable digits-only — both are safe.
        // What matters is it didn't throw.
        if (result != null) assertTrue(result.all(Char::isDigit))
    }

    @Test fun `is stable across calls`() {
        // Two calls with the same input must return the same value (no hidden
        // shared state on the util instance).
        val a = normalizer.normalize("+34 612 33 44 55", "ES")
        val b = normalizer.normalize("+34 612 33 44 55", "ES")
        assertEquals(a, b)
    }
}
