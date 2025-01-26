package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        repository.preparePlayer(url, onPrepared, onCompletion)
    }

    override fun start(onStarted: () -> Unit) {
        repository.startPlayer(onStarted)
    }

    override fun pause(onPaused: () -> Unit) {
        repository.pausePlayer(onPaused)
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        repository.playbackControl(onStarted, onPaused)
    }

    override fun release() {
        repository.releasePlayer()
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(repository.getCurrentPosition())
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }
}