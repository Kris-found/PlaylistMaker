package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.AudioPlayerRepository
import com.practicum.playlistmaker.player.model.PlayerState


class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            onCompletion()
        }
    }

    override fun startPlayer(onStarted: () -> Unit) {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
        onStarted()
    }

    override fun pausePlayer(onPaused: () -> Unit) {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
        onPaused()
    }

    override fun playbackControl(onStarted: () -> Unit, onPaused: () -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer(onPaused)
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer(onStarted)
            }

            else -> {}
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return (playerState == PlayerState.PLAYING)
    }
}