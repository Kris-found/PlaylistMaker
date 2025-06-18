package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.library.data.db.entity.PLAYLIST_TABLE
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM $PLAYLIST_TABLE ORDER BY timestamp DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM $PLAYLIST_TABLE WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}