package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.FAVORITE_TABLE
import com.practicum.playlistmaker.library.data.db.entity.TracksFavoriteEntity

@Dao
interface TrackFavoriteDao {

    @Insert(entity = TracksFavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TracksFavoriteEntity)

    @Delete
    suspend fun deleteTrack(track: TracksFavoriteEntity)

    @Query("SELECT * FROM $FAVORITE_TABLE ORDER BY timestamp DESC")
    suspend fun getTracks(): List<TracksFavoriteEntity>

    @Query("SELECT id FROM $FAVORITE_TABLE")
    suspend fun getTracksId(): List<Int>

    @Query("SELECT COUNT(*) FROM $FAVORITE_TABLE WHERE id = :trackId")
    suspend fun isTrackFavorite(trackId: Int): Boolean

}