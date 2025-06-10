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
import com.practicum.playlistmaker.player.presentation.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Tracks
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val KEY_TRACK_TAP = "TRACK"
        const val RADIUS_IMAGE = 8.0f
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private var songUrl: String = ""

    private lateinit var track: Tracks

    private val viewModelPlaer by viewModel<AudioPlayerViewModel> {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(KEY_TRACK_TAP) as Tracks

        if (track != null) bindTrackData(track) else finish()

        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.ibPlayButton.setOnClickListener {
            viewModelPlaer.playbackControl()
        }

        viewModelPlaer.getPlayerState().observe(this) {
            renderPlayer(it)
        }

        binding.ibLikeTrack.setOnClickListener {
            viewModelPlaer.onFavoriteClicked()
        }

        viewModelPlaer.getIsFavoriteTrackState().observe(this) {
            renderLikeButton(it)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isChangingConfigurations) {
            viewModelPlaer.stopPlayback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            viewModelPlaer.release()
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

    private fun renderPlayer(state: AudioPlayerState) {
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

            is AudioPlayerState.Stopped -> {
                binding.ibPlayButton.setImageResource(R.drawable.play_button)
            }
        }
    }

    private fun renderLikeButton(active: Boolean) {
        if (active) {
            binding.ibLikeTrack.setImageResource(R.drawable.active_like_button)
        } else {
            binding.ibLikeTrack.setImageResource(R.drawable.like_button)
        }
    }
}