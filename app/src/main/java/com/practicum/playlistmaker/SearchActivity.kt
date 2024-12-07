package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchAPI::class.java)

    private lateinit var arrowBackButton: MaterialToolbar
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var editText: EditText
    private lateinit var refreshButton:Button

    private lateinit var btnClearHistory: Button
    private lateinit var tvSearchHistory: TextView

    private lateinit var searchHistory: SearchHistory
    private lateinit var listener: OnSharedPreferenceChangeListener
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var searchAdapter: TrackAdapter
    private lateinit var rvForSearchTrack: RecyclerView

    private val trackList = ArrayList<Track>()

    private var savedQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvForSearchTrack = findViewById(R.id.rvSearchTrackList)

        arrowBackButton = findViewById(R.id.arrowBack)
        queryInput = findViewById(R.id.queryInput)
        editText = findViewById(R.id.queryInput)
        clearButton = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)

        tvSearchHistory = findViewById(R.id.tvSearchHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)

        searchAdapter = TrackAdapter(ArrayList()){
            searchHistory.addTrackToHistory(it)

            val audioPlayerIntent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra("TRACK", it)
            }

            startActivity(audioPlayerIntent)
        }

        sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        val historyTrackList = searchHistory.getHistoryTrack()

        rvForSearchTrack.adapter = searchAdapter

        updateHistoryRecyclerView()

        listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACK_HISTORY_KEY){
                updateHistoryRecyclerView()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            historyTrackList.clear()
            updateHistoryRecyclerView()
            rvForSearchTrack.setVisibility(false)
        }

        arrowBackButton.setNavigationOnClickListener {
            finish()
        }

        queryInput.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                if (queryInput.text.isNotEmpty()){
                    makeSearch()
                }
                true
            }
            false
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            trackList.clear()
            searchAdapter.notifyDataSetChanged()
            updateHistoryRecyclerView()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            makeSearch()
        }

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                updateHistoryRecyclerView()
            } else {
                historyVisibilityView(ViewVisibility.GONE)}
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if(queryInput.text.isEmpty()){
                    trackList.clear()
                }
                if (queryInput.hasFocus() && s?.isEmpty() == true) {
                    updateHistoryRecyclerView()
                    showPlaceholder(SearchState.EmptyInput)
                    } else {
                        historyVisibilityView(ViewVisibility.GONE)
                        rvForSearchTrack.setVisibility(false)
                    }
            }
            override fun afterTextChanged(s: Editable?) {
                savedQuery = s.toString()
            }
        }
        queryInput.addTextChangedListener(textWatcher)
    }

    private fun updateHistoryRecyclerView() {
        val historyTrackList = searchHistory.getHistoryTrack()
        if (historyTrackList.isNotEmpty() && queryInput.text.isEmpty()){
            searchAdapter.updateData(historyTrackList)
            historyVisibilityView(ViewVisibility.VISIBLE)
            rvForSearchTrack.setVisibility(true)
        } else {
            historyVisibilityView(ViewVisibility.GONE)
        }
    }

    private fun makeSearch(){
        iTunesService.search(queryInput.text.toString()).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.resultCount!! > 0){
                        showPlaceholder(SearchState.Success)
                        trackList.addAll(response.body()?.results!!)
                        searchAdapter.updateData(trackList)
                    } else{
                        showPlaceholder(SearchState.Empty)
                    }
                } else {
                    showPlaceholder(SearchState.NoConnection)
                    Toast.makeText( applicationContext,"Error: ${response.code()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showPlaceholder(SearchState.NoConnection)
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showPlaceholder(state: SearchState) {
        val layoutPlaceholderError = findViewById<LinearLayout>(R.id.layoutPlaceholderError)
        val placeholderImage = findViewById<ImageView>(R.id.placeholderImage)
        val placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)

        val errorMessage = "${getString(R.string.error_no_connection)}\n\n${getString(R.string.download_failed)}"

        trackList.clear()
        searchAdapter.notifyDataSetChanged()

        when(state) {
            SearchState.Empty -> {
                layoutPlaceholderError.setVisibility(true)
                rvForSearchTrack.setVisibility(false)
                refreshButton.setVisibility(false)
                placeholderImage.setImageResource(R.drawable.error_search)
                placeholderMessage.text = getString(R.string.error_search_empty)
            }
            SearchState.NoConnection -> {
                layoutPlaceholderError.setVisibility(true)
                refreshButton.setVisibility(true)
                rvForSearchTrack.setVisibility(false)
                placeholderImage.setImageResource(R.drawable.error_internet)
                placeholderMessage.text = errorMessage
            }
            SearchState.Success -> {
                layoutPlaceholderError.setVisibility(false)
                rvForSearchTrack.setVisibility(true)
            }
            SearchState.EmptyInput -> {
                layoutPlaceholderError.setVisibility(false)
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

    private fun historyVisibilityView(state: ViewVisibility){
        when(state){
            ViewVisibility.VISIBLE -> {
                btnClearHistory.setVisibility(true)
                tvSearchHistory.setVisibility(true)
            }

            ViewVisibility.GONE -> {
                btnClearHistory.setVisibility(false)
                tvSearchHistory.setVisibility(false)
            }
        }
    }

    private fun View.setVisibility(visible: Boolean){
        visibility = if(visible) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("key_search", editText.toString())
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

sealed interface SearchState {
    data object Empty: SearchState
    data object NoConnection: SearchState
    data object Success: SearchState
    data object EmptyInput: SearchState
}





