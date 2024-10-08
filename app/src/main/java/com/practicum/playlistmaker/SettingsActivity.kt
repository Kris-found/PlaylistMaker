package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val arrowBackButtonInSettings = findViewById<MaterialToolbar>(R.id.arrowBack)
        val buttonSwitch = findViewById<SwitchMaterial>(R.id.switchTheme)
        val buttonShareApp = findViewById<MaterialTextView>(R.id.shareApp)
        val buttonWriteToSupport = findViewById<MaterialTextView>(R.id.writeToSupport)
        val buttonAgreement = findViewById<MaterialTextView>(R.id.agreement)

        arrowBackButtonInSettings.setNavigationOnClickListener {
            finish()
        }

        buttonSwitch.setOnClickListener {
            if (buttonSwitch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        buttonShareApp.setOnClickListener {
            val linkApp = getString(R.string.linkToShareApp)
            val title = getString(R.string.titleShareApp)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkApp)

            startActivity(Intent.createChooser(shareIntent, title))
        }

        buttonWriteToSupport.setOnClickListener {
            val emailSupport = getString(R.string.emailSupport)
            val themeMessage = getString(R.string.supportThemeMessage)
            val message = getString(R.string.defaultSupportMessage)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailSupport))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, themeMessage)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)

            startActivity(shareIntent)
        }

        buttonAgreement.setOnClickListener {
            val offerYA = getString(R.string.appOffer)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(offerYA)

            startActivity(intent)
        }
    }
}