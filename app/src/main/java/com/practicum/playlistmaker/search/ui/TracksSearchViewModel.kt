package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.Utils.SingleEventLiveData
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import com.practicum.playlistmaker.search.domain.model.Tracks

class TracksSearchViewModel(
    application: Application,
    private val tracksInteractor: TracksInteractor
) : AndroidViewModel(application) {

    var currentRequestId: Int = 0
    private var currentQuery = ""

    private var isClickAllowed = true

    private val searchRunnable = Runnable {
        makeSearch(currentQuery)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun getScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    private val clickDebounceLiveData = SingleEventLiveData<Tracks>()
    fun getClickDebounce(): LiveData<Tracks> = clickDebounceLiveData

    init {
        setHistoryTrackList()
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        setHistoryTrackList()
    }

    fun setHistoryTrackList() {
        val historyTrackList = tracksInteractor.getHistoryTrack()
        screenStateLiveData.postValue(
            if (historyTrackList.isEmpty()) SearchScreenState.EmptyHistory else
                SearchScreenState.HistoryContent(historyTrackList)
        )
    }

    private fun addTrackToHistory(track: Tracks) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun makeSearch(query: String) {

        if (query.isNotEmpty()) {
            currentRequestId = (currentRequestId + 1) % MAX_REQUEST_ID
            val requestId = currentRequestId

            screenStateLiveData.postValue(SearchScreenState.Loading)

            val consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Tracks>?) {
                    if (requestId != currentRequestId) return
                    if (foundTracks.isNullOrEmpty()) {
                        screenStateLiveData.postValue(SearchScreenState.NothingFound)
                    } else {
                        screenStateLiveData.postValue(
                            SearchScreenState.Success(foundTracks)
                        )
                    }
                }

                override fun onError(errorMessage: String?) {
                    if (requestId != currentRequestId) return
                    screenStateLiveData.postValue(SearchScreenState.NoConnection)
                }
            }
            tracksInteractor.searchTracks(query, consumer)
        }
    }

    fun searchDebounce(query: String) {
        this.currentQuery = query
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_IN_SECONDS)
    }

    fun clickDebounce(track: Tracks) {
        if (isClickAllowed) {
            isClickAllowed = false
            addTrackToHistory(track)
            clickDebounceLiveData.value = track
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_IN_SECONDS)
        }
    }

    fun removeCallback() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_SECONDS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_IN_SECONDS = 1000L
        private const val MAX_REQUEST_ID = 1000

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(
                    this[APPLICATION_KEY] as Application,
                    Creator.provideTracksInteractor()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }
}