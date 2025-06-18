package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.TRACK_TABLE
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM $TRACK_TABLE ORDER BY timestamp DESC")
    suspend fun getAllTracks(): List<TrackEntity>

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM $TRACK_TABLE WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?
}