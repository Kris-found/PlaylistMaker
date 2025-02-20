package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.AudioPlayerState
import com.practicum.playlistmaker.player.model.PlayerState

class AudioPlayerViewModel(
    url: String,
    private val playerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

    private val handler = Handler(Looper.getMainLooper())

    init {
        preparePlayer(url)
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.prepare(
            url = previewUrl,
            onPrepared = {
                playerStateLiveData.postValue(AudioPlayerState.State(PlayerState.PREPARED))
            },
            onCompletion = {
                handler.removeCallbacks(trackTimerUpdater())
                playerStateLiveData.postValue(AudioPlayerState.State(PlayerState.PREPARED))
            }
        )
    }

    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (playerInteractor.isPlaying()) {
                playerStateLiveData.postValue(AudioPlayerState.Playing(playerInteractor.getCurrentPosition()))
                handler.postDelayed(this, DELAY_MILLIS)
            }
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl(
            onStarted = {
                playerStateLiveData.postValue(AudioPlayerState.State(PlayerState.PLAYING))
                handler.post(trackTimerUpdater())
            },
            onPaused = {
                playerStateLiveData.postValue(AudioPlayerState.State(PlayerState.PAUSED))
                handler.removeCallbacks(trackTimerUpdater())
            }
        )
    }

    fun pausePlayer() {
        playerInteractor.pause()
        playerStateLiveData.postValue(AudioPlayerState.State(PlayerState.PAUSED))
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