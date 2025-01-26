package com.practicum.playlistmaker.domain.model

sealed class ResultSearchTracks {
    data class Success(val tracks: List<Tracks>) : ResultSearchTracks()
    data class Error(val  message: String) : ResultSearchTracks()
}