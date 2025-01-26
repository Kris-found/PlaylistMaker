package com.practicum.playlistmaker.domain.api

interface AudioPlayerRepository {
    fun preparePlayer(
        url: String,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun startPlayer(onStarted: () -> Unit)
    fun pausePlayer(onPaused: () -> Unit)
    fun playbackControl(
        onStarted: () -> Unit = {},
        onPaused: () -> Unit = {}
    )

    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}