package com.practicum.playlistmaker.search.history.model

import com.practicum.playlistmaker.search.domain.model.Tracks

interface HistoryState {

    data object EmptyHistory : HistoryState

    data class HistoryContent(val history: List<Tracks>) : HistoryState
}