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

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query ("SELECT * FROM $TRACK_TABLE ORDER BY timestamp DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM $TRACK_TABLE")
    suspend fun getTracksId(): List<Int>

    @Query("SELECT COUNT(*) FROM $TRACK_TABLE WHERE id = :trackId")
    suspend fun isTrackFavorite(trackId: Int): Boolean

}