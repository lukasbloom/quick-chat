package com.quickwa

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.telephony.TelephonyManager
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

/**
 * Resolves the default ISO-3166-1 alpha-2 region used to interpret local
 * (non-international) phone numbers.
 *
 * Priority (per PRD):
 *   1. mobile network country (works when roaming)
 *   2. SIM country
 *   3. device configuration locale country
 *   4. fallback "US"
 *
 * Each candidate is validated against libphonenumber's supported-regions set
 * before being accepted — some carriers report exotic codes (e.g. "001"
 * meaning "world") that libphonenumber rejects. We skip those and fall through
 * to the next source rather than letting the normalizer Toast the user.
 *
 * Uses Resources configuration locale rather than `Locale.getDefault()` —
 * the latter can be overwritten by other libraries at runtime; the former
 * is the system-configured locale and is the right source for region
 * inference.
 *
 * networkCountryIso / simCountryIso are normal-permission accessors — no
 * READ_PHONE_STATE required.
 */
class CountryResolver(
    private val context: Context,
    private val util: PhoneNumberUtil = PhoneNumberUtil.getInstance()
) {

    fun resolve(): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val candidates = sequenceOf(
            tm?.networkCountryIso,
            tm?.simCountryIso,
            primaryConfigCountry()
        )
        return candidates
            .mapNotNull { it?.takeIf(String::isNotBlank)?.uppercase(Locale.ROOT) }
            .firstOrNull { util.supportedRegions.contains(it) }
            ?: DEFAULT_REGION
    }

    private fun primaryConfigCountry(): String? {
        val config: Configuration = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.takeIf { !it.isEmpty }?.get(0)?.country
        } else {
            @Suppress("DEPRECATION")
            config.locale.country
        }
    }

    companion object {
        private const val DEFAULT_REGION = "US"
    }
}
