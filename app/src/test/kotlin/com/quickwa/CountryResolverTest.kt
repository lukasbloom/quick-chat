package com.quickwa

import android.content.Context
import android.telephony.TelephonyManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class CountryResolverTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @Test fun `uses network country first when valid`() {
        shadowOf(tm).setNetworkCountryIso("es")
        shadowOf(tm).setSimCountryIso("us")
        assertEquals("ES", CountryResolver(context).resolve())
    }

    @Test fun `falls back to SIM when network is blank`() {
        shadowOf(tm).setNetworkCountryIso("")
        shadowOf(tm).setSimCountryIso("ar")
        assertEquals("AR", CountryResolver(context).resolve())
    }

    @Test fun `skips unsupported region code and falls through`() {
        // Some carriers report "001" (UN code for "world") which libphonenumber
        // does not recognise. The resolver should skip it rather than handing
        // it down to the normalizer.
        shadowOf(tm).setNetworkCountryIso("001")
        shadowOf(tm).setSimCountryIso("br")
        assertEquals("BR", CountryResolver(context).resolve())
    }

    @Test fun `falls back to US when nothing is resolvable`() {
        shadowOf(tm).setNetworkCountryIso("")
        shadowOf(tm).setSimCountryIso("")
        // Configuration locale on the Robolectric default app is en-US, so
        // the locale step also yields US — which happens to match the final
        // fallback. Either way, the contract is "returns US".
        assertEquals("US", CountryResolver(context).resolve())
    }
}
