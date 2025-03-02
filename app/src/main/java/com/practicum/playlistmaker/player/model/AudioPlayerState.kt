package com.practicum.playlistmaker.player.model

sealed class AudioPlayerState {

    object Prepared : AudioPlayerState()

    data class Playing(val currentPosition: String): AudioPlayerState()

    data class Paused(val lastPosition: String) : AudioPlayerState()

}
