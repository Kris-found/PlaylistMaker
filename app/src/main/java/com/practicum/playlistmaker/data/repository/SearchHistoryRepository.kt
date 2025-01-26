package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TracksDto

interface SearchHistoryRepository {
    fun saveTrackToHistory(tracks: ArrayList<TracksDto>)
    fun getHistoryTrack(): ArrayList<TracksDto>
    fun addTrackToHistory(track: TracksDto)
    fun clearHistory()
    fun registerChangeListener(onChange: () -> Unit)
}