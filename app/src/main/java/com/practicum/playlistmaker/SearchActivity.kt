package com.practicum.playlistmaker

import android.content.Context
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

    private lateinit var rvForSearchTrack: RecyclerView
    private lateinit var arrowBackButton: MaterialToolbar
    private lateinit var queryInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var editText: EditText
    private lateinit var refreshButton:Button

    private lateinit var lLHistorySearch: LinearLayout
    private lateinit var btnClearHistory: Button

    private lateinit var rvForHistoryTrack: RecyclerView
    private lateinit var searchHistory: SearchHistory
    private lateinit var listener: OnSharedPreferenceChangeListener
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val trackList = ArrayList<Track>()

    private var savedQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvForSearchTrack = findViewById(R.id.rvSearchTrackList)
        rvForHistoryTrack = findViewById(R.id.rvForHistoryTrack)

        arrowBackButton = findViewById(R.id.arrowBack)
        queryInput = findViewById(R.id.queryInput)
        clearButton = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)

        lLHistorySearch = findViewById(R.id.lLHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)

        searchAdapter = TrackAdapter(trackList){
            searchHistory.addTrackToHistory(it)
        }

        sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        val historyTrackList = searchHistory.getHistoryTrack()

        rvForSearchTrack.adapter = searchAdapter

        historyAdapter = TrackAdapter(historyTrackList){
            searchHistory.addTrackToHistory(it)
        }
        rvForHistoryTrack.adapter = historyAdapter

        updateHistoryRecyclerView()

        listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACK_HISTORY_KEY){
                updateHistoryRecyclerView()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            historyAdapter.notifyDataSetChanged()
            lLHistorySearch.visibility = View.GONE
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

        queryInput.setOnFocusChangeListener { view, hasFocus ->
            lLHistorySearch.visibility = if (hasFocus && queryInput.text.isEmpty() && historyTrackList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if(queryInput.text.isEmpty()){
                    trackList.clear()
                }
                lLHistorySearch.visibility = if (queryInput.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                savedQuery = s.toString()
            }
        }
        queryInput.addTextChangedListener(textWatcher)

    }
    private fun updateHistoryRecyclerView() {
        val historyTrackList = searchHistory.getHistoryTrack()
        historyAdapter.updateData(historyTrackList)
        lLHistorySearch.visibility = if(historyTrackList.isEmpty()) View.GONE else View.VISIBLE
        rvForSearchTrack.visibility = View.GONE
    }

    private fun makeSearch(){
        iTunesService.search(queryInput.text.toString()).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.resultCount!! > 0){
                        showPlaceholder("")
                        trackList.addAll(response.body()?.results!!)
                    } else{
                        showPlaceholder("empty")
                    }
                } else {
                    showPlaceholder("no_connection")
                    Toast.makeText( applicationContext,"Error: ${response.code()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showPlaceholder("no_connection")
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showPlaceholder(state: String) {

        val layoutPlaceholderError = findViewById<LinearLayout>(R.id.layoutPlaceholderError)
        val placeholderImage = findViewById<ImageView>(R.id.placeholderImage)
        val placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)

        val errorMessage = "${getString(R.string.error_no_connection)}\n\n${getString(R.string.download_failed)}"

        trackList.clear()
        searchAdapter.notifyDataSetChanged()

        when(state) {
            "empty" -> {
                layoutPlaceholderError.visibility = View.VISIBLE
                rvForSearchTrack.visibility = View.GONE
                refreshButton.visibility = View.GONE
                placeholderImage.setImageResource(R.drawable.error_search)
                placeholderMessage.text = getString(R.string.error_search_empty)
            }
            "no_connection" -> {
                layoutPlaceholderError.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
                rvForSearchTrack.visibility = View.GONE
                placeholderImage.setImageResource(R.drawable.error_internet)
                placeholderMessage.text = errorMessage
            }
            else -> {
                layoutPlaceholderError.visibility = View.GONE
                rvForSearchTrack.visibility = View.VISIBLE
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



