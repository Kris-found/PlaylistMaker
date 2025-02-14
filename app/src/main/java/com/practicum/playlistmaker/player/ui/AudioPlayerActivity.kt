package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.dpToPx
import com.practicum.playlistmaker.player.model.PlayerState
import com.practicum.playlistmaker.search.domain.model.Tracks

const val KEY_TRACK_TAP = "TRACK"

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val RADIUS_IMAGE = 8.0f
    }

    private lateinit var viewModel: AudioPlayerViewModel

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

        val track = intent.getParcelableExtra<Tracks>(KEY_TRACK_TAP)

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(track!!.previewUrl)
        )[AudioPlayerViewModel::class.java]

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
        arrowBackButton = findViewById(R.id.arrowBack)

        if (track != null) bindTrackData(track) else finish()

        arrowBackButton.setOnClickListener {
            finish()
        }

        ibPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getPlayerState().observe(this) {
            render(it)
        }

        viewModel.getCurrentPosition().observe(this) { position ->
            tvTrackTime.text = position
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            viewModel.release()
        }
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
            Glide.with(this)
                .load(track.getCoverArtwork)
                .placeholder(R.drawable.placeholder_312px)
                .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, this)))
                .into(ivCover)
        } else {
            ivCover.setImageResource(R.drawable.error_search)
        }
    }

    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.PREPARED -> {
                ibPlayButton.isEnabled = true
                tvTrackTime.text = getString(R.string.track_time_start)
                ibPlayButton.setImageResource(R.drawable.play_button)
            }

            PlayerState.PLAYING -> {
                ibPlayButton.setImageResource(R.drawable.pause_button)
            }

            PlayerState.PAUSED -> {
                ibPlayButton.setImageResource(R.drawable.play_button)
            }

            PlayerState.DEFAULT -> {}
        }
    }
}