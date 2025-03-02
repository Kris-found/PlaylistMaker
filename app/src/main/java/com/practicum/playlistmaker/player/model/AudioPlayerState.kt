package com.practicum.playlistmaker.player.model

sealed class AudioPlayerState {
    data class State(val playerState: PlayerState): AudioPlayerState()
    data class Playing(val currentPosition: String): AudioPlayerState()
}
