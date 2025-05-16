package com.practicum.playlistmaker.Utils

import android.content.Context
import android.util.TypedValue
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TracksDto
import com.practicum.playlistmaker.search.domain.model.Tracks

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()
}

fun Tracks.toDto(): TracksDto = TracksDto(
    trackName,
    artistName,
    trackTimeMillis,
    artworkUrl100,
    trackId,
    collectionName,
    releaseDate,
    primaryGenreName,
    country,
    previewUrl
)

fun TracksDto.toDomain(): Tracks = Tracks(
    trackName,
    artistName,
    trackTimeMillis,
    artworkUrl100,
    trackId,
    collectionName,
    releaseDate,
    primaryGenreName,
    country,
    previewUrl,
    isFavorite = false
)

fun Tracks.toTrackEntity(): TrackEntity = TrackEntity(
    id = trackId,
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName ?: "",
    releaseDate = releaseDate ?: "",
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    timestamp = System.currentTimeMillis()
)

fun TrackEntity.toTracksDomain(): Tracks = Tracks(
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    trackId = id,
    collectionName = collectionName,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    country = country,
    previewUrl = previewUrl,
    isFavorite = true
)