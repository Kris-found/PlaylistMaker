package com.practicum.playlistmaker.domain.api

interface SettingsThemeInteractor {
    fun isDarkThemeEnabled(): Boolean
    fun setTheme(darkTheme: Boolean)
    fun isSystemThemeSet(): Boolean
}