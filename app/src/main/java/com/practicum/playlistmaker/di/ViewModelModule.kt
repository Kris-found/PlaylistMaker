package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.library.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.playlist.content.presentation.PlaylistContentViewModel
import com.practicum.playlistmaker.playlist.create.presentation.CreatePlaylistViewModel
import com.practicum.playlistmaker.playlist.edit.presentation.EditPlaylistViewModel
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.presentation.TracksSearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TracksSearchViewModel(get())
    }

    viewModel { (track: Tracks) ->
        AudioPlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (playlistId: Long) ->
        EditPlaylistViewModel(playlistId, get())
    }

    viewModel { (playlistId: Long) ->
        PlaylistContentViewModel(playlistId, get(), get())
    }
}