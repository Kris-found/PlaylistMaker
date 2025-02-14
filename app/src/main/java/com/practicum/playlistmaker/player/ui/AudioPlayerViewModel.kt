package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.PlayerState

class AudioPlayerViewModel(
    url: String,
    private val playerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val currentPositionLiveData = MutableLiveData<String>()
    fun getCurrentPosition(): LiveData<String> = currentPositionLiveData

    private val handler = Handler(Looper.getMainLooper())

    init {
        preparePlayer(url)
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.prepare(
            url = previewUrl,
            onPrepared = { },
            onCompletion = {
                handler.removeCallbacks(trackTimerUpdater())
            }
        )
        playerStateLiveData.postValue(PlayerState.PREPARED)
    }

    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (playerInteractor.isPlaying()) {
                playerStateLiveData.postValue(PlayerState.PLAYING)
                currentPositionLiveData.postValue(playerInteractor.getCurrentPosition())
                handler.postDelayed(this, DELAY_MILLIS)
            }
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl(
            onStarted = {
                handler.post(trackTimerUpdater())
                playerStateLiveData.postValue(PlayerState.PLAYING)
            },
            onPaused = {
                handler.removeCallbacks(trackTimerUpdater())
                playerStateLiveData.postValue(PlayerState.PAUSED)
            }
        )
    }

    fun pausePlayer() {
        playerInteractor.pause()
        playerStateLiveData.postValue(PlayerState.PAUSED)
    }

    fun release() {
        handler.removeCallbacks(trackTimerUpdater())
        playerInteractor.release()
    }

    companion object {
        const val DELAY_MILLIS = 300L
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    url,
                    Creator.provideAudioPlayerInteractor()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }
}