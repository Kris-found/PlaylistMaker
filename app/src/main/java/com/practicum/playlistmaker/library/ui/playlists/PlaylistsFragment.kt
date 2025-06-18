package com.practicum.playlistmaker.library.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist
import com.practicum.playlistmaker.library.model.PlaylistState
import com.practicum.playlistmaker.library.presentation.PlaylistsViewModel
import com.practicum.playlistmaker.playlist.content.ui.PlaylistContentFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNewPlaylist.setOnClickListener {
            clickEventToOpenCreatePlaylist()
        }

        adapter = PlaylistAdapter {
            clickEventToOpenPlaylist(it)
        }

        binding.playlistView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistView.adapter = adapter

        viewModel.getPlaylistsState().observe(viewLifecycleOwner) {
            playlistsRender(it)
        }

        viewModel.getPlaylists()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun clickEventToOpenCreatePlaylist() {
        findNavController().navigate(R.id.action_mediaLibraryFragment_to_createPlaylistFragment)
    }

    private fun clickEventToOpenPlaylist(playlist: Playlist) {
        val bundle = Bundle().apply {
            putLong(PlaylistContentFragment.KEY_PLAYLIST_ID, playlist.id)
        }
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistContentFragment,
            bundle
        )
    }

    private fun playlistsRender(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> {
                binding.placeholderContainer.isVisible = false
                binding.playlistView.isVisible = true
                adapter.submitList(state.playlist)
            }

            is PlaylistState.Empty -> {
                binding.placeholderContainer.isVisible = true
                binding.playlistView.isVisible = false
            }
        }
    }
}