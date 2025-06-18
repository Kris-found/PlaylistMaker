package com.practicum.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.library.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistsState(): LiveData<PlaylistState> = playlistsLiveData

    init {
        viewModelScope.launch {
            getPlaylists()
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            interactor.getPlaylists().collect {
                if (it.isEmpty()) setState(PlaylistState.Empty) else setState(
                    PlaylistState.Content(
                        it
                    )
                )
            }
        }
    }

    private fun setState(state: PlaylistState) {
        playlistsLiveData.postValue(state)
    }
}