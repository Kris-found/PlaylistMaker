package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.Utils.Resource
import com.practicum.playlistmaker.search.domain.model.Tracks

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Tracks>>

    fun saveTrackToHistory(tracks: ArrayList<Tracks>)
    fun getHistoryTrack(): ArrayList<Tracks>
    fun addTrackToHistory(track: Tracks)
    fun clearHistory()
}