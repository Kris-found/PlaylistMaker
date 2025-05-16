package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Tracks>?, String?>>

    fun saveTrackToHistory(tracks: ArrayList<Tracks>)
    fun getHistoryTrack(): Flow<ArrayList<Tracks>>
    fun addTrackToHistory(track: Tracks)
    fun clearHistory()
}