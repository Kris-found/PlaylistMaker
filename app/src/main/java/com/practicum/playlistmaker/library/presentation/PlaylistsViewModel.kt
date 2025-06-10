package com.practicum.playlistmaker.library.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.library.model.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<PlaylistState>()
    fun getPlaylistsState(): LiveData<PlaylistState> = playlistsLiveData

    private val _savedImagePath = MutableLiveData<String?>()
    val savedImagePath: LiveData<String?> = _savedImagePath

    init {
        viewModelScope.launch {
            getPlaylists()
        }
    }

    fun createPlaylist(name: String, description: String, uri: String) {
        viewModelScope.launch {
            interactor.createPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    imageUri = uri,
                )
            )
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

    fun saveImage(uri: Uri) {
        viewModelScope.launch {
            val path = interactor.saveImage(uri)
            _savedImagePath.postValue(path)
        }
    }

    private fun setState(state: PlaylistState) {
        playlistsLiveData.postValue(state)
    }
}