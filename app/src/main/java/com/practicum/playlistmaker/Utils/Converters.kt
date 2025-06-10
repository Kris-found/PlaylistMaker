package com.practicum.playlistmaker.Utils

import android.content.Context
import android.util.TypedValue
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.data.db.entity.TracksFavoriteEntity
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.search.data.dto.TracksDto
import com.practicum.playlistmaker.search.domain.model.Tracks

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()
}

fun formattedTracksCount(count: Int): String {
    val ending = when {
        count % 10 == 1 -> "трек"
        count % 10 in 2..4 -> "трека"
        count % 100 in 11..14 -> "треков"
        else -> "треков"
    }
    return "$count $ending"
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

fun Tracks.toFavoriteEntity(): TracksFavoriteEntity = TracksFavoriteEntity(
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

fun TracksFavoriteEntity.toTracksDomain(): Tracks = Tracks(
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

fun Playlist.toPlaylistEntity(): PlaylistEntity = PlaylistEntity(
    id = id,
    name = name,
    description = description,
    imageUri = imageUri,
    tracksId = tracksId,
    tracksCount = tracksCount,
    timestamp = System.currentTimeMillis()
)

fun PlaylistEntity.toPlaylistDomain(): Playlist = Playlist(
    id = id,
    name = name,
    description = description,
    imageUri = imageUri,
    tracksId = tracksId,
    tracksCount = tracksCount,
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