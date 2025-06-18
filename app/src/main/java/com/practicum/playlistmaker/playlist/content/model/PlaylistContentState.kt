package com.practicum.playlistmaker.playlist.content.model

import com.practicum.playlistmaker.search.domain.model.Tracks

data class PlaylistContentState(
    val image: String,
    val name: String,
    val description: String,
    val tracksCount: Int,
    val totalDurationTracks: String,
    val tracks: List<Tracks>
)