package com.practicum.playlistmaker.library.model

import com.practicum.playlistmaker.library.domain.playlist.model.Playlist

sealed class PlaylistState {

    data object Empty : PlaylistState()

    data class Content(val playlist: List<Playlist>) : PlaylistState()

}