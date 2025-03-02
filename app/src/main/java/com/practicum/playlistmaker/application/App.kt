package com.practicum.playlistmaker.application

import android.app.Application
import android.content.res.Configuration
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        const val THEME_SWITCHER_PREFERENCES = "theme_switcher_preferences"
        const val APP_THEME_KEY = "theme_key"
    }

    private val settingsInteractor: SettingsThemeInteractor by inject()

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

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