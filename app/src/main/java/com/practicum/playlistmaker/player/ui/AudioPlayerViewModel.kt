package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.AudioPlayerState

class AudioPlayerViewModel(
    url: String,
    private val playerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    companion object {
        const val DELAY_MILLIS = 300L
    }

    private val playerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

    private var handler = Handler(Looper.getMainLooper())

    init {
        preparePlayer(url)
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.prepare(
            url = previewUrl,
            onPrepared = {
                playerStateLiveData.postValue(AudioPlayerState.Prepared)
            },
            onCompletion = {
                handler.removeCallbacks(trackTimerUpdater())
                playerStateLiveData.postValue(AudioPlayerState.Prepared)
            }
        )
    }

    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (playerInteractor.isPlaying()) {
                playerStateLiveData.postValue(
                    AudioPlayerState.Playing(
                        playerInteractor.getCurrentPosition()
                    )
                )
                handler.postDelayed(this, DELAY_MILLIS)
            }
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl(
            onStarted = {
                handler.post(trackTimerUpdater())
            },
            onPaused = {
                playerStateLiveData.postValue(
                    AudioPlayerState.Paused(
                        playerInteractor.getCurrentPosition()
                    )
                )
                handler.removeCallbacks(trackTimerUpdater())
            }
        )
    }

    fun pausePlayer() {
        playerInteractor.pause()
        playerStateLiveData.postValue(
            AudioPlayerState.Paused(
                playerInteractor.getCurrentPosition()
            )
        )
    }

    fun release() {
        handler.removeCallbacks(trackTimerUpdater())
        playerInteractor.release()
    }
}