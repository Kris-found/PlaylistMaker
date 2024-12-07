package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    companion object{
        const val KEY_TRACK_TAP = "TRACK"
        const val RADIUS_IMAGE = 8.0f
    }

    private lateinit var arrowBackButton: ImageButton
    private lateinit var tvCountryName: TextView
    private lateinit var tvGenreName: TextView
    private lateinit var tvYearValue: TextView
    private lateinit var tvAlbumName: TextView
    private lateinit var tvTrackDuration: TextView
    private lateinit var tvTrackTime: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvHeadAlbumName: TextView
    private lateinit var ivCover: ImageView
    private lateinit var tvAlbum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getParcelableExtra<Track>(KEY_TRACK_TAP)

        tvCountryName = findViewById(R.id.tvCountryName)
        tvGenreName = findViewById(R.id.tvGenreName)
        tvYearValue = findViewById(R.id.tvYearValue)
        tvAlbumName = findViewById(R.id.tvAlbumName)
        tvTrackDuration = findViewById(R.id.tvTrackDuration)
        tvTrackTime = findViewById(R.id.tvTrackTime)
        tvArtistName = findViewById(R.id.tvArtistName)
        tvHeadAlbumName = findViewById(R.id.tvHeadAlbumName)
        ivCover = findViewById(R.id.ivCover)
        tvAlbum = findViewById(R.id.tvAlbum)

        arrowBackButton = findViewById(R.id.arrowBack)

        arrowBackButton.setOnClickListener {
            finish()
        }

        if (track != null) bindTrackData(track) else finish()
    }

    private fun bindTrackData(track: Track){
        tvCountryName.text = track.country
        tvGenreName.text = track.primaryGenreName
        tvYearValue.text = track.releaseDate.substring(0, 4)
        tvAlbumName.text = track.collectionName ?: run {
            tvAlbumName.isVisible = false
            tvAlbum.isVisible = false
            return@run ""
        }
        tvTrackDuration.text = track.formattedTrackTime
        tvTrackTime.text = track.formattedTrackTime
        tvArtistName.text = track.artistName
        tvHeadAlbumName.text = track.collectionName ?: run {
            tvHeadAlbumName.isVisible = false
            return@run ""
        }
        Glide.with(applicationContext)
            .load(track.getCoverArtwork)
            .placeholder(R.drawable.placeholder_312px)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, this)))
            .into(ivCover)
    }
}

