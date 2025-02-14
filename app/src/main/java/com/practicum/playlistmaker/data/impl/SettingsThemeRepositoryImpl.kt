package com.practicum.playlistmaker.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.App.Companion.APP_THEME_KEY
import com.practicum.playlistmaker.domain.api.SettingsThemeRepository

class SettingsThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsThemeRepository {
    override fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(APP_THEME_KEY, false)
    }

    override fun saveTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if(darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })

        sharedPreferences.edit()
            .putBoolean(APP_THEME_KEY, darkTheme)
            .apply()
    }

    override fun isSystemThemeSet(): Boolean {
        return sharedPreferences.contains(APP_THEME_KEY)
    }
}