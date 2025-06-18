package com.practicum.playlistmaker.playlist.create.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    val savedImagePathLiveData = MutableLiveData<String?>()
    fun savedImagePath(): LiveData<String?> = savedImagePathLiveData

    fun createPlaylist(name: String, description: String) {
        val imageUri = savedImagePathLiveData.value.orEmpty()
        viewModelScope.launch {
            interactor.createPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    imageUri = imageUri,
                )
            )
        }
    }

    fun saveImage(uri: Uri) {
        viewModelScope.launch {
            val path = interactor.saveImage(uri)
            savedImagePathLiveData.postValue(path)
        }
    }
}