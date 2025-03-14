package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.dpToPx
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.model.AudioPlayerState
import com.practicum.playlistmaker.search.domain.model.Tracks
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val KEY_TRACK_TAP = "TRACK"

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val RADIUS_IMAGE = 8.0f
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private var songUrl: String = ""

    private val viewModel by viewModel<AudioPlayerViewModel> {
        parametersOf(songUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(KEY_TRACK_TAP) as Tracks?

        if (track != null) bindTrackData(track) else finish()

        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.ibPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getPlayerState().observe(this) {
            render(it)
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
        binding.tvCountryName.text = track.country
        binding.tvGenreName.text = track.primaryGenreName
        binding.tvYearValue.text = track.releaseDate?.take(4) ?: ""
        binding.tvAlbumName.text = track.collectionName ?: run {
            binding.tvAlbumName.isVisible = false
            binding.tvAlbum.isVisible = false
            return@run ""
        }
        binding.tvTrackDuration.text = track.formattedTrackTime
        binding.tvTrackTime.text = getString(R.string.track_time_start)
        binding.tvArtistName.text = track.artistName
        binding.tvHeadAlbumName.text = track.collectionName ?: run {
            binding.tvHeadAlbumName.isVisible = false
            return@run ""
        }
        songUrl = track.previewUrl

        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(this)
                .load(track.getCoverArtwork)
                .placeholder(R.drawable.placeholder_312px)
                .transform(RoundedCorners(dpToPx(RADIUS_IMAGE, this)))
                .into(binding.ivCover)
        } else {
            binding.ivCover.setImageResource(R.drawable.error_search)
        }
    }

    private fun render(state: AudioPlayerState) {
        when (state) {

            is AudioPlayerState.Prepared -> {
                binding.ibPlayButton.isEnabled = true
                binding.tvTrackTime.text = getString(R.string.track_time_start)
                binding.ibPlayButton.setImageResource(R.drawable.play_button)
            }

            is AudioPlayerState.Playing -> {
                binding.tvTrackTime.text = state.currentPosition
                binding.ibPlayButton.setImageResource(R.drawable.pause_button)
            }

            is AudioPlayerState.Paused -> {
                binding.ibPlayButton.setImageResource(R.drawable.play_button)
                binding.tvTrackTime.text = state.lastPosition
            }
        }
    }
}