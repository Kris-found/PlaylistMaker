package com.practicum.playlistmaker.player.domain

import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    private val dateFormat by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

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
        return dateFormat.format(repository.getCurrentPosition())
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }
}