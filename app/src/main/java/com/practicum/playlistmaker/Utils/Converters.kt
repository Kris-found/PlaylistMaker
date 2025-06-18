package com.practicum.playlistmaker.Utils

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.library.data.db.entity.TracksFavoriteEntity
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.search.data.dto.TracksDto
import com.practicum.playlistmaker.search.domain.model.Tracks
import java.util.Locale

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()
}

fun getLocalizedContext(base: Context, locale: Locale): Context {
    val config = Configuration(base.resources.configuration)
    config.setLocale(locale)
    return base.createConfigurationContext(config)
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

fun TrackEntity.toTrackDomain(): Tracks = Tracks(
    trackId = id,
    trackName = trackName,
    artistName = artistName,
    trackTimeMillis = trackTimeMillis,
    artworkUrl100 = artworkUrl100,
    collectionName = collectionName,
    country = country,
    releaseDate = releaseDate,
    primaryGenreName = primaryGenreName,
    previewUrl = previewUrl,
    isFavorite = false
)