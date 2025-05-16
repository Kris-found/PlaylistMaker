package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.Utils.SingleEventLiveData
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_SECONDS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_IN_SECONDS = 1000L
        private const val MAX_REQUEST_ID = 1000
    }

    var currentRequestId: Int = 0
    private var latestQuery: String? = null
    private var isClickAllowed = true
    private var searchJob: Job? = null

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

            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { pair ->
                        val foundTracks = pair.first
                        val errorMessage = pair.second

                        when {
                            requestId != currentRequestId -> return@collect

                            errorMessage != null -> screenStateLiveData.postValue(
                                SearchScreenState.NoConnection
                            )

                            foundTracks.isNullOrEmpty() -> screenStateLiveData.postValue(
                                SearchScreenState.NothingFound
                            )

                            else -> screenStateLiveData.postValue(
                                SearchScreenState.Success(foundTracks)
                            )
                        }
                    }
            }
        }
    }

    fun searchDebounce(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (latestQuery != query) {
                latestQuery = query
                delay(SEARCH_DEBOUNCE_DELAY_IN_SECONDS)
                makeSearch(query)
            }
        }
    }

    fun clickDebounce(track: Tracks) {
        if (isClickAllowed) {
            isClickAllowed = false
            addTrackToHistory(track)
            clickDebounceLiveData.value = track
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_IN_SECONDS)
                isClickAllowed = true
            }
        }
    }

    fun searchJobCancel() {
        searchJob?.cancel()
    }
}