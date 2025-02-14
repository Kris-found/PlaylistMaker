package com.practicum.playlistmaker.search.domain.model

sealed interface SearchState {

    data object Loading: SearchState

    data class Success(val tracks: List<Tracks>) : SearchState

    data object NothingFound : SearchState

    data object NoConnection : SearchState

}