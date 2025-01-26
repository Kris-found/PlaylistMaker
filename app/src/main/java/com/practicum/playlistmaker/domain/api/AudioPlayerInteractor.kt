package com.practicum.playlistmaker.domain.api

interface AudioPlayerInteractor {
    fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun start(onStarted: () -> Unit)
    fun pause(onPaused: () -> Unit = {})
    fun playbackControl(
        onStarted: () -> Unit,
        onPaused: () -> Unit
    )

    fun release()
    fun getCurrentPosition(): String
    fun isPlaying(): Boolean

}