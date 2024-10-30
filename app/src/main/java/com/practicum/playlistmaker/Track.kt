package com.practicum.playlistmaker

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    val formattedTrackTime: String
        get() {
            Log.d("Track", "trackTimeMillis: $trackTimeMillis")
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        }
}