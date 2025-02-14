package com.practicum.playlistmaker.Creator

import android.app.Application
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.playlistmaker.data.App.Companion.THEME_SWITCHER_PREFERENCES
import com.practicum.playlistmaker.data.network.RetrofitNetworkClientImpl
import com.practicum.playlistmaker.data.impl.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.impl.SEARCH_HISTORY_PREFERENCES
import com.practicum.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.impl.SettingsThemeRepositoryImpl
import com.practicum.playlistmaker.data.impl.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.SettingsThemeInteractor
import com.practicum.playlistmaker.domain.api.SettingsThemeRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private lateinit var application: Application
    fun setApp(application: Application){
        Creator.application = application
    }

    private fun getTracksRepository() : TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClientImpl(), SearchHistoryRepositoryImpl(
            application.getSharedPreferences(
            SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE
        ))
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
        ))
    }
    fun provideSettingsThemeInteractor(): SettingsThemeInteractor {
        return SettingsThemeInteractorImpl(getSettingsThemeRepository())
    }
}