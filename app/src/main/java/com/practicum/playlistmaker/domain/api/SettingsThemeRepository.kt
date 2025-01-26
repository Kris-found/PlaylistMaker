package com.practicum.playlistmaker.domain.api

interface SettingsThemeRepository {
    fun getTheme(): Boolean
    fun saveTheme(darkTheme: Boolean)
    fun isSystemThemeSet(): Boolean
}