package com.practicum.playlistmaker

import android.media.MediaPlayer
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
import java.text.SimpleDateFormat
import java.util.Locale

const val KEY_TRACK_TAP = "TRACK"

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val RADIUS_IMAGE = 8.0f
    }

    private var playerState = PlayerState.DEFAULT
    private var mediaPlayer = MediaPlayer()

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

        val track = intent.getParcelableExtra<Track>(KEY_TRACK_TAP)

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
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(trackTimerUpdater())
        mediaPlayer.release()
    }

    private fun bindTrackData(track: Track) {
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

        Glide.with(applicationContext)
            .load(track.getCoverArtwork)
            .placeholder(R.drawable.placeholder_312px)
            .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, this)))
            .into(ivCover)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            ibPlayButton.isEnabled = true
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            tvTrackTime.text = getString(R.string.track_time_start)
            ibPlayButton.setImageResource(R.drawable.play_button)
            playerState = PlayerState.PREPARED
            handler.removeCallbacks(trackTimerUpdater())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.post(trackTimerUpdater())
        ibPlayButton.setImageResource(R.drawable.pause_button)
        playerState = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        handler.removeCallbacks(trackTimerUpdater())
        mediaPlayer.pause()
        ibPlayButton.setImageResource(R.drawable.play_button)
        playerState = PlayerState.PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun trackTimerUpdater() = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.PLAYING) {
                tvTrackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, 300L)
            }
        }
    }
}

enum class PlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}