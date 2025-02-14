package com.practicum.playlistmaker.data.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.model.ResultSearchTracks
import com.practicum.playlistmaker.domain.model.Tracks
import com.practicum.playlistmaker.Utils.toDomain
import com.practicum.playlistmaker.Utils.toDto

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryRepository: SearchHistoryRepository
) : TracksRepository {

    override fun searchTracks(expression: String): ResultSearchTracks {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            200 -> ResultSearchTracks.Success((response as TracksSearchResponse).results.map {
                it.toDomain()
            })

            -1 -> ResultSearchTracks.Error("No internet connection")

            else -> ResultSearchTracks.Error("Error: ${response.resultCode}")
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Tracks>) {
        val dtoTracks = tracks.map { it.toDto() }
        searchHistoryRepository.saveTrackToHistory(ArrayList(dtoTracks))
    }

    override fun getHistoryTrack(): ArrayList<Tracks> {
        val dtoTracks = searchHistoryRepository.getHistoryTrack()
        return ArrayList(dtoTracks.map { it.toDomain() })
    }

    override fun addTrackToHistory(track: Tracks) {
        val dtoTrack = track.toDto()
        searchHistoryRepository.addTrackToHistory(dtoTrack)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }

    override fun registerChangeListener(onChange: () -> Unit) {
        searchHistoryRepository.registerChangeListener(onChange)
    }
}