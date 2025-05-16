package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrackToFavorites(track:Tracks)

    suspend fun removeTrackFromFavorites(track: Tracks)

    fun getAllFavoriteTracks(): Flow<List<Tracks>>

    fun isTrackFavorite(trackId: Int): Flow<Boolean>
}