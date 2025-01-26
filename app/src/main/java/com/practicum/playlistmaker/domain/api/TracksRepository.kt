package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.ResultSearchTracks
import com.practicum.playlistmaker.domain.model.Tracks

interface TracksRepository {
    fun searchTracks(expression: String): ResultSearchTracks

    fun saveTrackToHistory(tracks: ArrayList<Tracks>)
    fun getHistoryTrack(): ArrayList<Tracks>
    fun addTrackToHistory(track: Tracks)
    fun clearHistory()
    fun registerChangeListener(onChange: () -> Unit)
}