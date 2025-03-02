package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.Utils.Resource
import com.practicum.playlistmaker.search.domain.model.Tracks
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            val result = repository.searchTracks(expression)
            when (result) {
                is Resource.Success -> consumer.consume(result.data)
                is Resource.Error -> consumer.onError(result.message)
            }
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Tracks>) {
        repository.saveTrackToHistory(tracks)
    }

    override fun getHistoryTrack(): ArrayList<Tracks> {
        return repository.getHistoryTrack()
    }

    override fun addTrackToHistory(track: Tracks) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}