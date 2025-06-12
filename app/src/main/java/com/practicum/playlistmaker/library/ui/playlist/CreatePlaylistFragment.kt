package com.practicum.playlistmaker.library.ui.playlist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.library.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CreatePlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var savedImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.savedImagePath.observe(viewLifecycleOwner) { path ->
            path?.let {
                binding.ivCoverAlbum.setImageURI(Uri.fromFile(File(it)))
            }
        }

        binding.editTextName.addTextChangedListener { s ->
            val hasText = s.isNullOrEmpty()

            binding.namePlaylist.error = if (hasText) getString(R.string.required_field) else null
            binding.createPlaylist.isEnabled = !hasText
        }

        binding.editTextDescription.hideKeyboardOnDone(requireContext())
        binding.editTextName.hideKeyboardOnDone(requireContext())

        binding.arrowBack.setOnClickListener {
            onBackPressed()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                onBackPressed()
        }

        binding.ivCoverAlbum.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylist.setOnClickListener {
            viewModel.createPlaylist(
                name = binding.editTextName.text.toString(),
                description = binding.editTextDescription.text.toString()
            )

            showPlaylistCreatedSnackbar(name = binding.editTextName.text.toString())
            findNavController().popBackStack()
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.saveImage(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun EditText.hideKeyboardOnDone(context: Context) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus()
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
                true
            } else false
        }
    }

    private fun onBackPressed() {

        if (!binding.editTextName.text.isNullOrEmpty() ||
            !binding.editTextDescription.text.isNullOrEmpty() ||
            !savedImagePath.isNullOrEmpty()
        ) {
            Log.d("BackPressDebug", "isAdded: $isAdded, isResumed: $isResumed, context = $context")
            MaterialAlertDialogBuilder(requireContext(), R.style.PlaylistAlertDialog)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setNeutralButton((getString(R.string.dialog_button_cancel)), null)
                .setPositiveButton(getString(R.string.dialog_button_exit)) { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showPlaylistCreatedSnackbar(name: String) {
        val message = getString(R.string.playlist_name_created, name)
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                MaterialColors.getColor(
                    requireView(),
                    R.attr.playlistSnackbarBackground
                )
            )
            .setTextColor(
                MaterialColors.getColor(
                    requireView(),
                    R.attr.playlistSnackbarTextColor
                )
            )
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            gravity = Gravity.CENTER
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }

        snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        snackbar.show()
    }
}