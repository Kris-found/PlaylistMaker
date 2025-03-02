package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.player.ui.KEY_TRACK_TAP
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import com.practicum.playlistmaker.search.domain.model.Tracks
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModel<TracksSearchViewModel>()

    private lateinit var searchAdapter: TrackAdapter
    private val trackList = ArrayList<Tracks>()
    private var savedQuery: String = ""
    private var shouldUpdateHistory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchAdapter = TrackAdapter(ArrayList()) {
            viewModel.clickDebounce(it)
        }

        binding.rvSearchTrackList.adapter = searchAdapter

        onTrackClickEvents()

        viewModel.getScreenState().observe(this) {
            renderScreenState(it)
        }

        binding.arrowBack.setNavigationOnClickListener {
            finish()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.queryInput.setText("")
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            viewModel.setHistoryTrackList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.refreshButton.setOnClickListener {
            viewModel.makeSearch(binding.queryInput.text.toString())
        }

        binding.queryInput.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.makeSearch(view.text.toString())
                true
            }
            false
        }

        binding.queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.queryInput.text.isEmpty()) {
                viewModel.setHistoryTrackList()
            } else {
                historyVisibilityView(ViewVisibility.GONE)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)

                if (binding.queryInput.hasFocus() && s?.isEmpty() == true) {
                    binding.progressBar.isVisible = false
                    viewModel.removeCallback()
                    viewModel.currentRequestId++
                    viewModel.setHistoryTrackList()
                } else {
                    historyVisibilityView(ViewVisibility.GONE)
                    binding.rvSearchTrackList.isVisible = false
                    binding.layoutPlaceholderError.isVisible = false
                    viewModel.searchDebounce(
                        query = s.toString()
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.queryInput.addTextChangedListener(textWatcher)
    }
    override fun onResume() {
        super.onResume()
        if (shouldUpdateHistory && binding.queryInput.text.isEmpty()) {
            viewModel.setHistoryTrackList()
            shouldUpdateHistory = false
        }
    }

    private fun onTrackClickEvents() {
        viewModel.getClickDebounce().observe(this) { track ->

            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(KEY_TRACK_TAP, track)
            }
            startActivity(audioPlayerIntent)

            shouldUpdateHistory = true
        }
    }

    private fun renderScreenState(state: SearchScreenState) {

        val errorMessage =
            "${getString(R.string.error_no_connection)}\n\n${getString(R.string.download_failed)}"

        binding.progressBar.isVisible = state is SearchScreenState.Loading

        when (state) {
            is SearchScreenState.Loading -> {
                binding.rvSearchTrackList.isVisible = false
            }

            is SearchScreenState.Success -> {
                binding.layoutPlaceholderError.isVisible = false
                searchAdapter.updateData(ArrayList(state.tracks))
                binding.rvSearchTrackList.isVisible = true
            }

            is SearchScreenState.NothingFound -> {
                binding.layoutPlaceholderError.isVisible = true
                binding.rvSearchTrackList.isVisible = false
                binding.refreshButton.isVisible = false
                binding.placeholderImage.setImageResource(R.drawable.error_search)
                binding.placeholderMessage.text = getString(R.string.error_search_empty)
            }

            is SearchScreenState.NoConnection -> {
                binding.layoutPlaceholderError.isVisible = true
                binding.refreshButton.isVisible = true
                binding.rvSearchTrackList.isVisible = false
                binding.placeholderImage.setImageResource(R.drawable.error_internet)
                binding.placeholderMessage.text = errorMessage
            }

            is SearchScreenState.EmptyHistory -> {
                historyVisibilityView(ViewVisibility.GONE)
                binding.rvSearchTrackList.isVisible = false
                binding.layoutPlaceholderError.isVisible = false
            }

            is SearchScreenState.HistoryContent -> {
                searchAdapter.updateData(ArrayList(state.history))
                historyVisibilityView(ViewVisibility.VISIBLE)
                binding.rvSearchTrackList.isVisible = true
                binding.layoutPlaceholderError.isVisible = false
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun historyVisibilityView(state: ViewVisibility) {
        when (state) {
            ViewVisibility.VISIBLE -> {
                binding.btnClearHistory.isVisible = true
                binding.tvSearchHistory.isVisible = true
            }

            ViewVisibility.GONE -> {
                binding.btnClearHistory.isVisible = false
                binding.tvSearchHistory.isVisible = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("key_search", binding.queryInput.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedQuery = savedInstanceState.getString("key_search", "")
        binding.queryInput.setText(savedQuery)
    }
}

enum class ViewVisibility {
    VISIBLE,
    GONE
}





