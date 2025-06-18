package com.practicum.playlistmaker.library.data

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.practicum.playlistmaker.Utils.toPlaylistDomain
import com.practicum.playlistmaker.Utils.toPlaylistEntity
import com.practicum.playlistmaker.Utils.toTrackDomain
import com.practicum.playlistmaker.Utils.toTrackEntity
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val gson: Gson, private val app: Application
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist): Long {
        return appDatabase.playlistDao().insertPlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlist.toPlaylistEntity())
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists().map { it.toPlaylistDomain() }
        emit(playlists)
    }

    override suspend fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId).toPlaylistDomain()
        emit(playlist)
    }

    override suspend fun getTracksInPlaylist(playlistId: Long): Flow<List<Tracks>> = flow {
        val tracks = appDatabase.trackDao().getAllTracks()
            .filter {
                it.id.toString() in appDatabase.playlistDao().getPlaylistById(playlistId).tracksId
            }
            .map { it.toTrackDomain() }
        emit(tracks)
    }

    override suspend fun addTrackToPlaylist(track: Tracks, playlist: Playlist) {
        val currentTrackId = if (playlist.tracksId.isNotEmpty()) {
            gson.fromJson(playlist.tracksId, Array<Int>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        currentTrackId.add(track.trackId)

        val updatePlaylistEntity = playlist.toPlaylistEntity().copy(
            tracksId = gson.toJson(currentTrackId),
            tracksCount = playlist.tracksCount + 1
        )
        appDatabase.trackDao().insertTrack(track.toTrackEntity())
        appDatabase.playlistDao().updatePlaylist(updatePlaylistEntity)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val currentTrackId =
            gson.fromJson(playlist.tracksId, Array<Int>::class.java).toMutableList()

        currentTrackId.remove(trackId)

        val updatePlaylistEntity = playlist.copy(
            tracksId = gson.toJson(currentTrackId),
            tracksCount = currentTrackId.size
        )
        appDatabase.playlistDao().updatePlaylist(updatePlaylistEntity)
        cleanUpTrackFromTable(trackId)
    }

    override suspend fun cleanUpTrackFromTable(trackId: Int) {
        val allPlaylists = appDatabase.playlistDao().getPlaylists()

        val allTracksIds = allPlaylists.map {
            gson.fromJson(it.tracksId, Array<Int>::class.java).toList()
        }
        val isUsedInPlaylists = allTracksIds.any {
            trackId in it
        }

        if (!isUsedInPlaylists) {
            val track = appDatabase.trackDao().getTrackById(trackId)
            if (track != null) appDatabase.trackDao().deleteTrack(track)
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        appDatabase.playlistDao().deletePlaylist(playlist)
        val tracksId = gson.fromJson(playlist.tracksId, Array<Int>::class.java)
        tracksId.forEach {
            cleanUpTrackFromTable(it)
        }
    }

    override suspend fun saveImage(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val filePath =
                File(app.getExternalFilesDir(Environment.DIRECTORY_PICTURES), CHILD_FILE_PATH)
            if (!filePath.exists()) filePath.mkdirs()

            val fileName = "cover_${System.currentTimeMillis()}.jpg"
            val file = File(filePath, fileName)

            val bitmap = Glide.with(app)
                .asBitmap()
                .load(uri)
                .submit()
                .get()

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            file.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val CHILD_FILE_PATH = "Album"
    }
}
