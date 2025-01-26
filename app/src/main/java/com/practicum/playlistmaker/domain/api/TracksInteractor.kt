package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.Tracks

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun saveTrackToHistory(tracks: ArrayList<Tracks>)
    fun getHistoryTrack(): ArrayList<Tracks>
    fun addTrackToHistory(track: Tracks)
    fun clearHistory()
    fun registerChangeListener(onChange: () -> Unit)

    interface TracksConsumer {
        fun consume(foundTracks: List<Tracks>)
        fun onError(errorMessage: String)
    }
}