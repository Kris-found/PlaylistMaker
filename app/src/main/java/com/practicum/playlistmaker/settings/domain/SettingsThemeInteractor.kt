package com.practicum.playlistmaker.settings.domain

interface SettingsThemeInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun setTheme(darkTheme: Boolean)
    fun isSystemThemeSet(): Boolean
}