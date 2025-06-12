package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FAVORITE_TABLE)
data class TracksFavoriteEntity(
    @PrimaryKey
    val id: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val timestamp: Long,
)

const val FAVORITE_TABLE = "favorite_table"