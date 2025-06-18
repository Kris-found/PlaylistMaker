package com.practicum.playlistmaker.playlist.content.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.PluralsRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.getLocalizedContext
import com.practicum.playlistmaker.databinding.FragmentPlaylistContentBinding
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.playlist.content.model.PlaylistContentState
import com.practicum.playlistmaker.playlist.content.presentation.PlaylistContentViewModel
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

class PlaylistContentFragment : Fragment() {

    companion object {
        const val KEY_PLAYLIST_ID = "playlist_id"
    }

    private lateinit var binding: FragmentPlaylistContentBinding
    private lateinit var trackAdapter: TrackAdapter

    private val playlistId by lazy {
        requireArguments().getLong(KEY_PLAYLIST_ID)
    }

    private val viewModel by viewModel<PlaylistContentViewModel> {
        parametersOf(playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter(
            ArrayList(),
            clickListener = { track -> onTrackClickEventToPlayer(track) },
            longClickListener = { track -> showDialogDeleteTrack(track) }
        )

        binding.rvTracks.adapter = trackAdapter

        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.editPlaylistBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnShare.setOnClickListener {
            sharePlaylist()
        }

        binding.btnMore.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.tvShareInBottomSheet.setOnClickListener {
            sharePlaylist()
        }

        binding.tvDeletePlaylist.setOnClickListener {
            showDialogDeletePlaylist()
        }

        binding.tvEditInfo.setOnClickListener {
            onPlaylistClickEventToEdit()
        }

        viewModel.getPlaylistDataState().observe(viewLifecycleOwner) {
            renderPlaylist((it))
        }

        viewModel.loadPlaylist()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylist()
    }

    private fun renderPlaylist(state: PlaylistContentState) {

        val resourceTrackCount = getLocalResourceRu(
            R.plurals.track_count,
            state.tracksCount,
            state.tracksCount
        )

        val resourceTracksDuration = getLocalResourceRu(
            R.plurals.track_duration_minutes,
            state.totalDurationTracks.toIntOrNull() ?: 0,
            state.totalDurationTracks.toIntOrNull() ?: 0
        )
        with(binding) {
            tvPlaylistName.text = state.name
            playlistView.tvTitle.text = state.name

            if (state.description.isNullOrBlank()) {
                tvPlaylistDescription.isVisible = false
            } else {
                tvPlaylistDescription.text = state.description
                tvPlaylistDescription.isVisible = true
            }

            tvTotalCountTracks.text = resourceTrackCount
            playlistView.countTracks.text = resourceTrackCount

            tvOverallDurationTracks.text = resourceTracksDuration

            if (state.image.isNullOrBlank()) {
                binding.ivCover.scaleType = ImageView.ScaleType.CENTER_INSIDE
            } else {
                binding.ivCover.scaleType = ImageView.ScaleType.CENTER_CROP
            }

            Glide.with(this@PlaylistContentFragment)
                .load(state.image)
                .placeholder(R.drawable.placeholder_312px)
                .into(ivCover)

            Glide.with(this@PlaylistContentFragment)
                .load(state.image)
                .placeholder(R.drawable.placeholder)
                .into(playlistView.ivCover)

            trackAdapter.updateData(ArrayList(state.tracks))
            if (state.tracks.isNullOrEmpty()) {
                tvEmptyPlaylist.isVisible = true
                rvTracks.isVisible = false
            } else {
                tvEmptyPlaylist.isVisible = false
                rvTracks.isVisible = true
            }
        }
    }

    private fun onTrackClickEventToPlayer(track: Tracks) {
        val bundle = Bundle().apply {
            putParcelable(AudioPlayerFragment.KEY_TRACK_TAP, track)
        }

        findNavController().navigate(
            R.id.action_playlistContentFragment_to_audioPlayerFragment,
            bundle
        )
    }

    private fun onPlaylistClickEventToEdit() {
        val bundle = Bundle().apply {
            putLong(KEY_PLAYLIST_ID, playlistId)
        }
        findNavController().navigate(
            R.id.action_playlistContentFragment_to_editPlaylistFragment,
            bundle
        )
    }

    private fun showDialogDeleteTrack(track: Tracks) {
        MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistAlertDialog)
            .setMessage(getString(R.string.want_to_delete_track))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deleteTrack(track.trackId)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun showDialogDeletePlaylist() {
        MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistAlertDialog)
            .setMessage(getString(R.string.want_to_delete_playlist))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().popBackStack()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun sharePlaylist() {
        viewModel.getPlaylistDataState().value?.let { state ->
            if (state.tracks.isNotEmpty()) {
                messageFromSharePlaylist(state)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_tracks_in_playlist_from_share),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun messageFromSharePlaylist(state: PlaylistContentState) {
        val resourceTrack = getLocalResourceRu(
            R.plurals.track_count,
            state.tracksCount,
            state.tracksCount
        )

        val message = buildString {
            appendLine(state.name)
            if (!state.description.isNullOrBlank()) appendLine(state.description)
            appendLine(resourceTrack)
            appendLine()
            state.tracks.forEachIndexed { index, tracks ->
                appendLine("${index + 1}. ${tracks.artistName} - ${tracks.trackName} - ${tracks.formattedTrackTime}.")
            }
        }
        viewModel.sharePlaylist(message, getString(R.string.share_playlist))
    }

    private fun getLocalResourceRu(
        @PluralsRes pluralResId: Int,
        quantity: Int,
        vararg formatArgs: Any
    ): String {
        val contextRu = getLocalizedContext(binding.root.context, Locale("ru"))
        return contextRu.resources.getQuantityString(pluralResId, quantity, *formatArgs)
    }
}