package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.player.ui.KEY_TRACK_TAP
import com.practicum.playlistmaker.search.domain.model.SearchState
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.history.model.HistoryState

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: TracksSearchViewModel

    private lateinit var arrowBackButton: MaterialToolbar
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var editText: EditText
    private lateinit var refreshButton: Button

    private lateinit var btnClearHistory: Button
    private lateinit var tvSearchHistory: TextView

    private lateinit var progressBar: ProgressBar
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var rvForSearchTrack: RecyclerView
    private lateinit var layoutPlaceholderError: LinearLayout

    private val trackList = ArrayList<Tracks>()

    private var savedQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(
            this,
            TracksSearchViewModel.getViewModelFactory()
        )[TracksSearchViewModel::class.java]

        rvForSearchTrack = findViewById(R.id.rvSearchTrackList)

        arrowBackButton = findViewById(R.id.arrowBack)
        queryInput = findViewById(R.id.queryInput)
        editText = findViewById(R.id.queryInput)
        clearButton = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)
        progressBar = findViewById(R.id.progressBar)

        tvSearchHistory = findViewById(R.id.tvSearchHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        layoutPlaceholderError = findViewById(R.id.layoutPlaceholderError)

        searchAdapter = TrackAdapter(ArrayList()) {
            viewModel.clickDebounce(it)
        }

        rvForSearchTrack.adapter = searchAdapter

        onTrackClickEvents()

        viewModel.getHistoryState().observe(this) {
            renderHistory(it)
        }

        viewModel.getSearchState().observe(this) {
            renderSearch(it)
        }

        arrowBackButton.setNavigationOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            viewModel.setHistoryTrackList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            viewModel.makeSearch(queryInput.text.toString())
        }

        queryInput.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.makeSearch(view.text.toString())
                true
            }
            false
        }

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                viewModel.setHistoryTrackList()
            } else {
                historyVisibilityView(ViewVisibility.GONE)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (queryInput.hasFocus() && s?.isEmpty() == true) {
                    progressBar.isVisible = false
                    viewModel.removeCallback()
                    viewModel.currentRequestId++
                    viewModel.setHistoryTrackList()
                } else {
                    historyVisibilityView(ViewVisibility.GONE)
                    rvForSearchTrack.isVisible = false
                    layoutPlaceholderError.isVisible = false
                    viewModel.searchDebounce(
                        query = s.toString()
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        queryInput.addTextChangedListener(textWatcher)
    }

    private fun onTrackClickEvents() {
        viewModel.getClickDebounce().observe(this) { track ->
            if (queryInput.text.toString().isEmpty()) {
                viewModel.setHistoryTrackList()
            }
            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(KEY_TRACK_TAP, track)
            }
            startActivity(audioPlayerIntent)
        }
    }

    private fun renderSearch(searchState: SearchState) {
        val placeholderImage = findViewById<ImageView>(R.id.placeholderImage)
        val placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)

        val errorMessage =
            "${getString(R.string.error_no_connection)}\n\n${getString(R.string.download_failed)}"

        progressBar.isVisible = searchState is SearchState.Loading

        when (searchState) {
            is SearchState.Loading -> {
                rvForSearchTrack.isVisible = false
            }

            is SearchState.Success -> {
                layoutPlaceholderError.isVisible = false
                searchAdapter.updateData(ArrayList(searchState.tracks))
                rvForSearchTrack.isVisible = true
            }

            is SearchState.NothingFound -> {
                layoutPlaceholderError.isVisible = true
                rvForSearchTrack.isVisible = false
                refreshButton.isVisible = false
                placeholderImage.setImageResource(R.drawable.error_search)
                placeholderMessage.text = getString(R.string.error_search_empty)
            }

            is SearchState.NoConnection -> {
                layoutPlaceholderError.isVisible = true
                refreshButton.isVisible = true
                rvForSearchTrack.isVisible = false
                placeholderImage.setImageResource(R.drawable.error_internet)
                placeholderMessage.text = errorMessage
            }
        }
    }

    private fun renderHistory(state: HistoryState) {
        when (state) {
            is HistoryState.HistoryContent -> {
                searchAdapter.updateData(ArrayList(state.history))
                historyVisibilityView(ViewVisibility.VISIBLE)
                rvForSearchTrack.isVisible = true
                layoutPlaceholderError.isVisible = false
            }

            is HistoryState.EmptyHistory -> {
                historyVisibilityView(ViewVisibility.GONE)
                rvForSearchTrack.isVisible = false
                layoutPlaceholderError.isVisible = false
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
                btnClearHistory.isVisible = true
                tvSearchHistory.isVisible = true
            }

            ViewVisibility.GONE -> {
                btnClearHistory.isVisible = false
                tvSearchHistory.isVisible = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("key_search", editText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedQuery = savedInstanceState.getString("key_search", "")
        editText.setText(savedQuery)
    }
}

enum class ViewVisibility {
    VISIBLE,
    GONE
}





