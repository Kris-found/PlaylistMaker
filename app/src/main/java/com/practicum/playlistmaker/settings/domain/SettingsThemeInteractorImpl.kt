package com.practicum.playlistmaker.settings.domain

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