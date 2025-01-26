package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SettingsThemeInteractor
import com.practicum.playlistmaker.domain.api.SettingsThemeRepository

class SettingsThemeInteractorImpl(private val repository: SettingsThemeRepository) :
    SettingsThemeInteractor {
    override fun isDarkThemeEnabled(): Boolean {
        return repository.getTheme()
    }

    override fun setTheme(darkTheme: Boolean) {
        repository.saveTheme(darkTheme)
    }

    override fun isSystemThemeSet(): Boolean {
        return repository.isSystemThemeSet()
    }
}