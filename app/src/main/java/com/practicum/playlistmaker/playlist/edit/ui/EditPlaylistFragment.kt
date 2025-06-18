package com.practicum.playlistmaker.playlist.edit.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.content.ui.PlaylistContentFragment.Companion.KEY_PLAYLIST_ID
import com.practicum.playlistmaker.playlist.create.ui.CreatePlaylistFragment
import com.practicum.playlistmaker.playlist.edit.presentation.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlaylistFragment : CreatePlaylistFragment() {

    private val playlistId by lazy {
        requireArguments().getLong(KEY_PLAYLIST_ID)
    }

    override val viewModel by viewModel<EditPlaylistViewModel> {
        parametersOf(playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.createPlaylist.setOnClickListener {
            viewModel.updatePlaylist(
                name = binding.editTextName.text.toString(),
                description = binding.editTextDescription.text.toString()
            )
            findNavController().popBackStack()
        }

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlist ->
            with(binding) {
                if (!playlist.imageUri.isNullOrEmpty()) {
                    ivCoverAlbum.setImageURI(Uri.fromFile(File(playlist.imageUri)))
                }
                editTextName.setText(playlist.name)
                editTextDescription.setText(playlist.description)
                arrowBack.setTitle(getString(R.string.edit))
                createPlaylist.setText(getString(R.string.save))
            }
        }
    }
}