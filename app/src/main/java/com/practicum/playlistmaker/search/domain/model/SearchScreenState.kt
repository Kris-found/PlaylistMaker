package com.practicum.playlistmaker.search.domain.model

sealed class SearchScreenState {

    data object Loading : SearchScreenState()

    data class Success(val tracks: List<Tracks>) : SearchScreenState()

    data object NothingFound : SearchScreenState()

    data object NoConnection : SearchScreenState()

    data object EmptyHistory : SearchScreenState()

    data class HistoryContent(val history: List<Tracks>) : SearchScreenState()

}