package com.quickwa

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat

/**
 * Turns arbitrary text containing a phone number into an E.164 digits-only
 * string suitable for `https://wa.me/<digits>`. Returns null when no valid
 * number can be extracted.
 *
 * Rules (per PRD):
 *   - strip noise (spaces, dashes, parens, dots, tabs) — libphonenumber's
 *     parser does this for us
 *   - numbers starting with `+` or `00` are international; do not prepend
 *     a country code
 *   - otherwise parse against the provided default region (from CountryResolver)
 */
class PhoneNumberNormalizer(
    private val util: PhoneNumberUtil = PhoneNumberUtil.getInstance()
) {
    fun normalize(text: String?, defaultRegion: String): String? {
        if (text.isNullOrBlank()) return null

        directParse(preClean(text), defaultRegion)?.let { return it }
        return findInText(text, defaultRegion)
    }

    private fun directParse(input: String, region: String): String? = try {
        val parsed = util.parse(input, region)
        if (util.isValidNumber(parsed))
            util.format(parsed, PhoneNumberFormat.E164).removePrefix("+")
        else null
    } catch (_: NumberParseException) {
        null
    }

    private fun findInText(raw: String, region: String): String? {
        val match = util.findNumbers(raw, region).firstOrNull { util.isValidNumber(it.number()) }
            ?: return null
        return util.format(match.number(), PhoneNumberFormat.E164).removePrefix("+")
    }

    private fun preClean(raw: String): String {
        val trimmed = raw.trim()
        return when {
            trimmed.startsWith("+") -> trimmed
            trimmed.startsWith("00") -> "+" + trimmed.drop(2)
            else -> trimmed
        }
    }
}
