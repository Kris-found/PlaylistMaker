package com.practicum.playlistmaker.playlist.content.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.practicum.playlistmaker.playlist.content.model.PlaylistContentState
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistContentViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val playlistDataLiveData = MutableLiveData<PlaylistContentState>()
    fun getPlaylistDataState(): LiveData<PlaylistContentState> = playlistDataLiveData

    fun loadPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                val tracks = playlistInteractor.getTracksInPlaylist(playlistId).first()

                val totalInMillis = tracks.sumOf { it.trackTimeMillis }
                val formattedTime = SimpleDateFormat("mm", Locale.getDefault())
                    .format(totalInMillis)

                playlistDataLiveData.postValue(
                    PlaylistContentState(
                        image = playlist.imageUri,
                        name = playlist.name,
                        description = playlist.description,
                        tracksCount = tracks.size,
                        totalDurationTracks = formattedTime,
                        tracks = tracks,
                    )
                )
            }
        }
    }

    fun deleteTrack(trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(playlistId, trackId)
            loadPlaylist()
        }
    }

    fun sharePlaylist(message: String, title: String) {
        sharingInteractor.sharePlaylist(message, title)
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }
}