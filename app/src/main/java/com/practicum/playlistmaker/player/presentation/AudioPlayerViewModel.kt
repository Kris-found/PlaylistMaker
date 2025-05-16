package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.FavoriteTracksInteractor
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    val track: Tracks,
    private val playerInteractor: AudioPlayerInteractor,
    private val favoriteInteractor: FavoriteTracksInteractor,
) : ViewModel() {

    companion object {
        const val DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null
    private var isTrackCompleted = false

    private val playerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

    private val isFavoriteTrackLiveData = MutableLiveData<Boolean>()
    fun getIsFavoriteTrackState(): LiveData<Boolean> = isFavoriteTrackLiveData

    init {
        preparePlayer(track.previewUrl)
        viewModelScope.launch {
            favoriteInteractor.isTrackFavorite(track).collect {
                isFavoriteTrackLiveData.value = it
            }
        }
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            val isFavorite = isFavoriteTrackLiveData.value ?: false

            if (!isFavorite) {
                favoriteInteractor.addTrackToFavorites(track)
            } else {
                favoriteInteractor.removeTrackFromFavorites(track)
            }

            isFavoriteTrackLiveData.value = !isFavorite
        }
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