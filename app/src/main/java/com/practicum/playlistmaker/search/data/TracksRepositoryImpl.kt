package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.Utils.Resource
import com.practicum.playlistmaker.Utils.toDomain
import com.practicum.playlistmaker.Utils.toDto
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.history.data.SearchHistoryRepository

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryRepository: SearchHistoryRepository
) : TracksRepository {

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val ERROR = "Error"
    }

    override fun searchTracks(expression: String): Resource<List<Tracks>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    it.toDomain()
                })
            }

            -1 -> {
                Resource.Error(NO_INTERNET_CONNECTION)
            }

            else -> {
                Resource.Error("$ERROR: ${response.resultCode}")
            }
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
}