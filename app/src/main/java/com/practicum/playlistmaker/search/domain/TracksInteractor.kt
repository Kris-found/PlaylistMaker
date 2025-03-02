package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.model.Tracks

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun saveTrackToHistory(tracks: ArrayList<Tracks>)
    fun getHistoryTrack(): ArrayList<Tracks>
    fun addTrackToHistory(track: Tracks)
    fun clearHistory()

    interface TracksConsumer {
        fun consume(foundTracks: List<Tracks>?)
        fun onError(errorMessage: String?)
    }
}