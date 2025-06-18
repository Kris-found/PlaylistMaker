package com.practicum.playlistmaker.library.domain.playlist

import android.net.Uri
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist): Long {
        return repository.createPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksInPlaylist(playlistId: Long): Flow<List<Tracks>> {
        return repository.getTracksInPlaylist(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        repository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun cleanUpTrackFromTable(trackId: Int) {
        repository.cleanUpTrackFromTable(trackId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Tracks, playlist: Playlist) {
        repository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun saveImage(uri: Uri): String? {
        return repository.saveImage(uri)
    }
}