package com.practicum.playlistmaker.presentation.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Tracks
import com.practicum.playlistmaker.Utils.dpToPx

const val KEY_TRACK_TAP = "TRACK"

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val RADIUS_IMAGE = 8.0f
    }

    private val getPlayerInteractor = Creator.provideAudioPlayerInteractor()

    private val handler = Handler(Looper.getMainLooper())

    private var songUrl: String = ""

    private lateinit var arrowBackButton: ImageButton
    private lateinit var tvCountryName: TextView
    private lateinit var tvGenreName: TextView
    private lateinit var tvYearValue: TextView
    private lateinit var tvAlbumName: TextView
    private lateinit var tvTrackDuration: TextView
    private lateinit var ibPlayButton: ImageButton
    private lateinit var tvTrackTime: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvHeadAlbumName: TextView
    private lateinit var ivCover: ImageView
    private lateinit var tvAlbum: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        tvCountryName = findViewById(R.id.tvCountryName)
        tvGenreName = findViewById(R.id.tvGenreName)
        tvYearValue = findViewById(R.id.tvYearValue)
        tvAlbumName = findViewById(R.id.tvAlbumName)
        tvTrackDuration = findViewById(R.id.tvTrackDuration)
        tvTrackTime = findViewById(R.id.tvTrackTime)
        ibPlayButton = findViewById(R.id.ibPlayButton)
        tvArtistName = findViewById(R.id.tvArtistName)
        tvHeadAlbumName = findViewById(R.id.tvHeadAlbumName)
        ivCover = findViewById(R.id.ivCover)
        tvAlbum = findViewById(R.id.tvAlbum)

        val track = intent.getParcelableExtra<Tracks>(KEY_TRACK_TAP)

        if (track != null) {
            bindTrackData(track)
            preparePlayer()
        } else finish()

        arrowBackButton = findViewById(R.id.arrowBack)

        arrowBackButton.setOnClickListener {
            finish()
        }

        ibPlayButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        getPlayerInteractor.pause()
        ibPlayButton.setImageResource(R.drawable.play_button)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(trackTimerUpdater())
        getPlayerInteractor.release()
    }

    private fun bindTrackData(track: Tracks) {
        tvCountryName.text = track.country
        tvGenreName.text = track.primaryGenreName
        tvYearValue.text = track.releaseDate.take(4)
        tvAlbumName.text = track.collectionName ?: run {
            tvAlbumName.isVisible = false
            tvAlbum.isVisible = false
            return@run ""
        }
        tvTrackDuration.text = track.formattedTrackTime
        tvTrackTime.text = getString(R.string.track_time_start)
        tvArtistName.text = track.artistName
        tvHeadAlbumName.text = track.collectionName ?: run {
            tvHeadAlbumName.isVisible = false
            return@run ""
        }
        songUrl = track.previewUrl

        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(applicationContext)
                .load(track.getCoverArtwork)
                .placeholder(R.drawable.placeholder_312px)
                .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, this)))
                .into(ivCover)
        } else {
            ivCover.setImageResource(R.drawable.error_search)
        }
    }

    private fun preparePlayer() {
        getPlayerInteractor.prepare(
            url = songUrl,
            onPrepared = { ibPlayButton.isEnabled = true },
            onCompletion = {
                tvTrackTime.text = getString(R.string.track_time_start)
                ibPlayButton.setImageResource(R.drawable.play_button)
                handler.removeCallbacks(trackTimerUpdater())
            }
        )
    }

    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (getPlayerInteractor.isPlaying()) {
                tvTrackTime.text = getPlayerInteractor.getCurrentPosition()
                handler.postDelayed(this, 300L)
            }
        }
    }

    private fun playbackControl() {
        getPlayerInteractor.playbackControl(
            onStarted = {
                handler.post(trackTimerUpdater())
                ibPlayButton.setImageResource(R.drawable.pause_button)
            },
            onPaused = {
                handler.removeCallbacks(trackTimerUpdater())
                ibPlayButton.setImageResource(R.drawable.play_button)
            }
        )
    }
}