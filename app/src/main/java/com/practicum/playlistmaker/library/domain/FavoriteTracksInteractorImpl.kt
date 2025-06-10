package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val repository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    override suspend fun addTrackToFavorites(track: Tracks) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Tracks) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Tracks>> {
        return repository.getAllFavoriteTracks()
    }

    override fun isTrackFavorite(track: Tracks): Flow<Boolean> {
        return repository.isTrackFavorite(track.trackId)
    }

}