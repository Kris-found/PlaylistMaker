package com.practicum.playlistmaker.settings.domain

interface SettingsThemeRepository {
    fun getTheme(): Boolean
    fun saveTheme(darkTheme: Boolean)
    fun isSystemThemeSet(): Boolean
}