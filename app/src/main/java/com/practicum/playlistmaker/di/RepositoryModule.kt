package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.SettingsThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsThemeRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SettingsThemeRepository> {
        SettingsThemeRepositoryImpl(get(named("theme_switcher_prefs")))
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

}