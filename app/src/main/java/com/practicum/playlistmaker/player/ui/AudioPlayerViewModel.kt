package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.AudioPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    url: String,
    private val playerInteractor: AudioPlayerInteractor,
) : ViewModel() {

    companion object {
        const val DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null
    private var isTrackCompleted = false

    private val playerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

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
                isTrackCompleted = true
                timerJob?.cancel()
                playerStateLiveData.postValue(AudioPlayerState.Prepared)
            }
        )
    }

    private fun trackTimerUpdater() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                playerStateLiveData.postValue(
                    AudioPlayerState.Playing(
                        playerInteractor.getCurrentPosition()
                    )
                )
                delay(DELAY_MILLIS)
            }
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl(
            onStarted = {
                trackTimerUpdater()
            },
            onPaused = {
                pausePlayer()
            }
        )
    }

    private fun pausePlayer() {
        pauseAndPostState(
            AudioPlayerState.Paused(
                playerInteractor.getCurrentPosition()
            )
        )
    }

    fun stopPlayback() {
        pauseAndPostState(
            AudioPlayerState.Stopped
        )
    }

    private fun pauseAndPostState(state: AudioPlayerState) {
        timerJob?.cancel()
        playerInteractor.pause()
        playerStateLiveData.postValue(state)
    }

    fun release() {
        timerJob?.cancel()
        timerJob = null
        playerInteractor.release()
    }
}