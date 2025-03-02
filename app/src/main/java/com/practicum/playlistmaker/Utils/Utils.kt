package com.practicum.playlistmaker.Utils

import android.content.Context
import android.util.TypedValue
import com.practicum.playlistmaker.search.data.dto.TracksDto
import com.practicum.playlistmaker.search.domain.model.Tracks

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun Tracks.toDto(): TracksDto = TracksDto(
    trackName, artistName, trackTimeMillis, artworkUrl100, trackId, collectionName, releaseDate,
    primaryGenreName, country, previewUrl
)

fun TracksDto.toDomain(): Tracks = Tracks(
    trackName, artistName, trackTimeMillis, artworkUrl100, trackId, collectionName, releaseDate,
    primaryGenreName, country, previewUrl
)