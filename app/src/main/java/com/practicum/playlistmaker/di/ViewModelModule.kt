package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.AudioPlayerViewModel
import com.practicum.playlistmaker.search.ui.TracksSearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksSearchViewModel(get())
    }

    viewModel { (url: String) ->
        AudioPlayerViewModel(url, get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}