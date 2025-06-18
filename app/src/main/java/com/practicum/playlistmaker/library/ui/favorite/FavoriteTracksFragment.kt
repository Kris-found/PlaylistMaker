package com.practicum.playlistmaker.library.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.library.model.FavoriteTracksState
import com.practicum.playlistmaker.library.presentation.FavoriteTracksViewModel
import com.practicum.playlistmaker.player.ui.AudioPlayerFragment
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private lateinit var binding: FragmentFavoriteTracksBinding
    private lateinit var adapter: TrackAdapter

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(ArrayList()) {
            onTrackClickEvents(it)
        }

        binding.rvFavoriteList.adapter = adapter

        viewModel.getFavoriteTracks()

        viewModel.favoriteTracksState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    private fun render(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> {
                adapter.updateData(ArrayList(state.tracks))
                binding.rvFavoriteList.isVisible = true
                binding.placeholderContainer.isVisible = false
            }

            FavoriteTracksState.Empty -> {
                binding.rvFavoriteList.isVisible = false
                binding.placeholderContainer.isVisible = true
            }
        }
    }

    private fun onTrackClickEvents(track: Tracks) {
        val bundle = Bundle().apply {
            putParcelable(AudioPlayerFragment.KEY_TRACK_TAP, track)
        }
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            bundle
        )
    }
}