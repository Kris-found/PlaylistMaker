package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

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

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        viewModel.getThemeAppLiveData().observe(this) { isDarkThemeEnabled ->
            buttonSwitch.isChecked = isDarkThemeEnabled
        }

        buttonSwitch.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        buttonShareApp.setOnClickListener {
            val linkApp = getString(R.string.linkToShareApp)
            val title = getString(R.string.titleShareApp)
            viewModel.shareApp(linkApp, title)
        }

        buttonWriteToSupport.setOnClickListener {
            val emailSupport = getString(R.string.emailSupport)
            val themeMessage = getString(R.string.supportThemeMessage)
            val message = getString(R.string.defaultSupportMessage)

            val emailData = EmailData(
                email = emailSupport,
                subject = themeMessage,
                message = message
            )
            viewModel.openSupport(emailData)
        }

        buttonAgreement.setOnClickListener {
            val offerYA = getString(R.string.appOffer)
            viewModel.openTermsLink(offerYA)
        }
    }
}