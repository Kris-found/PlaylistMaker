package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.playlistmaker.application.App.Companion.THEME_SWITCHER_PREFERENCES
import com.practicum.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClientImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.history.data.SEARCH_HISTORY_PREFERENCES
import com.practicum.playlistmaker.search.history.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.settings.data.SettingsThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsThemeInteractor
import com.practicum.playlistmaker.settings.domain.SettingsThemeInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsThemeRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractorImpl

object Creator {
    private lateinit var application: Application
    fun setApp(application: Application) {
        Creator.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClientImpl(application), SearchHistoryRepositoryImpl(
                application.getSharedPreferences(
                    SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE
                )
            )
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    private fun getSettingsThemeRepository(): SettingsThemeRepository {
        return SettingsThemeRepositoryImpl(
            application.getSharedPreferences(
                THEME_SWITCHER_PREFERENCES, MODE_PRIVATE
            )
        )
    }

    fun provideSettingsThemeInteractor(): SettingsThemeInteractor {
        return SettingsThemeInteractorImpl(getSettingsThemeRepository())
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
}