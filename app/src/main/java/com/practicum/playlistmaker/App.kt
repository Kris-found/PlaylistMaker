package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(THEME_SWITCHER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        getSharedPreferences(THEME_SWITCHER_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()
    }

    companion object {
        const val THEME_SWITCHER_PREFERENCES = "theme_switcher_preferences"
        const val THEME_KEY = "theme_key"
    }
}