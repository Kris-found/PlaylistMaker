package com.practicum.playlistmaker.search.history.data

import com.practicum.playlistmaker.search.data.dto.TracksDto

interface SearchHistoryRepository {
    fun saveTrackToHistory(tracks: ArrayList<TracksDto>)
    fun getHistoryTrack(): List<TracksDto>
    fun addTrackToHistory(track: TracksDto)
    fun clearHistory()
}