package com.practicum.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.db.dao.TrackDao
import com.practicum.playlistmaker.library.data.db.dao.TrackFavoriteDao
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.data.db.entity.TracksFavoriteEntity

@Database(
    version = 3,
    entities = [TracksFavoriteEntity::class, PlaylistEntity::class, TrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDaoFavorite(): TrackFavoriteDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackDao(): TrackDao
}