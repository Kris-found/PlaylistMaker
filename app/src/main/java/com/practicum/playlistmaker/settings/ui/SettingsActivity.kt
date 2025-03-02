package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBack.setNavigationOnClickListener {
            finish()
        }

        viewModel.getThemeAppLiveData().observe(this) { isDarkThemeEnabled ->
            binding.switchTheme.isChecked = isDarkThemeEnabled
        }

        binding.switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareApp.setOnClickListener {
            val linkApp = getString(R.string.linkToShareApp)
            val title = getString(R.string.titleShareApp)
            viewModel.shareApp(linkApp, title)
        }

        binding.writeToSupport.setOnClickListener {
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

        binding.agreement.setOnClickListener {
            val offerYA = getString(R.string.appOffer)
            viewModel.openTermsLink(offerYA)
        }
    }
}