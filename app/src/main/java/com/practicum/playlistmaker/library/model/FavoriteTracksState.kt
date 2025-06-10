package com.practicum.playlistmaker.library.model

import com.practicum.playlistmaker.search.domain.model.Tracks

sealed class FavoriteTracksState {

    data object Empty : FavoriteTracksState()

    data class Content(val tracks: List<Tracks>) : FavoriteTracksState()

}
