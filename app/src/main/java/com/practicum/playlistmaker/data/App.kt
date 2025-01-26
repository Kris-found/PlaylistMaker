package com.practicum.playlistmaker.data

import android.app.Application
import android.content.res.Configuration
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.domain.api.SettingsThemeInteractor

class App : Application() {

    companion object {
        const val THEME_SWITCHER_PREFERENCES = "theme_switcher_preferences"
        const val APP_THEME_KEY = "theme_key"
    }

    var darkTheme = false
    private lateinit var settingsInteractor: SettingsThemeInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.setApp(application = this)
        settingsInteractor = Creator.provideSettingsThemeInteractor()

        darkTheme = if (!settingsInteractor.isSystemThemeSet()) {
            getSystemTheme()
        } else {
            settingsInteractor.isDarkThemeEnabled()
        }
        settingsInteractor.setTheme(darkTheme)
    }

    private fun getSystemTheme(): Boolean {
        val nightModeFlags =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkTheme = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        return darkTheme
    }
}