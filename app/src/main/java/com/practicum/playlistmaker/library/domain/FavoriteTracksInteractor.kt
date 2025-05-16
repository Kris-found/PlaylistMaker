package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrackToFavorites(track: Tracks)

    suspend fun removeTrackFromFavorites(track: Tracks)

    fun getAllFavoriteTracks(): Flow<List<Tracks>>

    fun isTrackFavorite(track: Tracks): Flow<Boolean>
}