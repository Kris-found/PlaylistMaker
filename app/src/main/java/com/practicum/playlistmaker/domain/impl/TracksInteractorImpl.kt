package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.model.ResultSearchTracks
import com.practicum.playlistmaker.domain.model.Tracks
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            val result = repository.searchTracks(expression)
            when (result) {
                is ResultSearchTracks.Success -> consumer.consume(result.tracks)
                is ResultSearchTracks.Error -> consumer.onError(result.message)
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

    override fun registerChangeListener(onChange: () -> Unit) {
        repository.registerChangeListener(onChange)
    }
}