package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.Utils.Resource
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Tracks>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Tracks>) {
        repository.saveTrackToHistory(tracks)
    }

    override fun getHistoryTrack(): Flow<ArrayList<Tracks>> {
        return repository.getHistoryTrack()
    }

    override fun addTrackToHistory(track: Tracks) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}