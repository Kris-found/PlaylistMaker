package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeAppLiveData().observe(viewLifecycleOwner) { isDarkThemeEnabled ->
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
