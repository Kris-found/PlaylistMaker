package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsThemeInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    private val settingsInteractor: SettingsThemeInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val themeAppLiveData = MutableLiveData<Boolean>()
    fun getThemeAppLiveData(): LiveData<Boolean> = themeAppLiveData

    init {
        themeAppLiveData.value = settingsInteractor.isDarkThemeEnabled()
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        settingsInteractor.setTheme(isDarkThemeEnabled)
        themeAppLiveData.value = isDarkThemeEnabled
    }

    fun shareApp(linkApp: String, title: String) {
        sharingInteractor.shareApp(linkApp, title)
    }

    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }

    fun openTermsLink(linkTerms: String) {
        sharingInteractor.orenTerms(linkTerms)
    }
}