package com.quickwa

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView

/**
 * Launcher-visible "About / Uninstall" screen. The rest of the app is a
 * Theme.NoDisplay intent shim — without this screen, the app has no icon
 * in the app drawer and is hard to remove. This activity is the only
 * place we present any UI to the user.
 *
 * Plain-Activity + programmatic layout deliberately, to avoid pulling in
 * AppCompat or Compose for a single static screen.
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildLayout())
    }

    private fun buildLayout(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setBackgroundColor(Color.parseColor("#312E81"))
        setPadding(dp(32), dp(64), dp(32), dp(40))
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        addView(ImageView(context).apply {
            setImageResource(R.mipmap.ic_launcher)
            layoutParams = LinearLayout.LayoutParams(dp(96), dp(96)).apply {
                bottomMargin = dp(20)
            }
        })

        addView(textView(getString(R.string.app_name)).apply {
            textSize = 28f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        })

        addView(textView("v${versionName()}").apply {
            textSize = 13f
            setTextColor(Color.parseColor("#C7D2FE"))
            setPadding(0, dp(2), 0, dp(32))
        })

        addView(textView(getString(R.string.about_howto)).apply {
            textSize = 15f
            setTextColor(Color.parseColor("#E0E7FF"))
            setLineSpacing(0f, 1.4f)
            gravity = Gravity.START
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                bottomMargin = dp(40)
            }
        })

        addView(Button(context).apply {
            text = getString(R.string.about_open_settings)
            setOnClickListener { openAppSettings() }
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        })
    }

    private fun textView(s: String) = TextView(this).apply {
        text = s
        gravity = Gravity.CENTER
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", packageName, null))
        startActivity(intent)
    }

    private fun versionName(): String =
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0).versionName ?: "?"

    private fun dp(value: Int): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics
    ).toInt()
}
