package com.practicum.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Parcelable {
    val formattedTrackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    val getCoverArtwork: String
        get() = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

}