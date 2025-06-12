package com.practicum.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.library.model.PlaylistState
import com.practicum.playlistmaker.player.domain.AudioPlayerInteractor
import com.practicum.playlistmaker.player.model.AddingTrackState
import com.practicum.playlistmaker.player.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    val track: Tracks,
    private val playerInteractor: AudioPlayerInteractor,
    private val favoriteInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        const val DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null

    private val playerStateLiveData = MutableLiveData<AudioPlayerState>()
    fun getPlayerState(): LiveData<AudioPlayerState> = playerStateLiveData

    private val isFavoriteTrackLiveData = MutableLiveData<Boolean>()
    fun getIsFavoriteTrackState(): LiveData<Boolean> = isFavoriteTrackLiveData

    private val playlistStateLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistState(): LiveData<PlaylistState> = playlistStateLiveData

    private val isAddedTrackStateLiveData = MutableLiveData<AddingTrackState>()
    fun getIsTrackAddedState(): LiveData<AddingTrackState> = isAddedTrackStateLiveData

    init {
        preparePlayer(track.previewUrl)
        viewModelScope.launch {
            favoriteInteractor.isTrackFavorite(track).collect {
                isFavoriteTrackLiveData.value = it
            }
        }
        getPlaylist()
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.prepare(
            url = previewUrl,
            onPrepared = {
                playerStateLiveData.postValue(AudioPlayerState.Prepared)
            },
            onCompletion = {
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

    fun getPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) playlistStateLiveData.postValue(PlaylistState.Empty)
                else playlistStateLiveData.postValue(PlaylistState.Content(it))
            }
        }
    }

    fun addTrackToPlaylist(track: Tracks, playlist: Playlist) {
        if (track.trackId.toString() in playlist.tracksId) isAddedTrackStateLiveData.postValue(
            AddingTrackState.TrackAlreadyAdded(name = playlist.name)
        ) else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
                getPlaylist()
                isAddedTrackStateLiveData.postValue(
                    AddingTrackState.TrackAdded(name = playlist.name)
                )
            }
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

    private fun release() {
        timerJob?.cancel()
        timerJob = null
        playerInteractor.release()
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }
}