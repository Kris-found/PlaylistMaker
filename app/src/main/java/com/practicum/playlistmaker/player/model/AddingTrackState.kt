package com.practicum.playlistmaker.player.model

sealed class AddingTrackState {

    data class TrackAdded(val name: String) : AddingTrackState()

    data class TrackAlreadyAdded(val name: String) : AddingTrackState()
}