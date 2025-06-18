package com.practicum.playlistmaker.playlist.edit.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.playlist.create.presentation.CreatePlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val interactor: PlaylistInteractor
) :
    CreatePlaylistViewModel(interactor) {

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun getPlaylistState(): LiveData<Playlist> = playlistLiveData

    init {
        viewModelScope.launch {
            interactor.getPlaylistById(playlistId).collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun updatePlaylist(name: String, description: String) {
        val updateData = playlistLiveData.value?.copy(
            name = name,
            description = description,
            imageUri = savedImagePathLiveData.value.orEmpty()
        ) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            interactor.updatePlaylist(updateData)
        }
    }
}