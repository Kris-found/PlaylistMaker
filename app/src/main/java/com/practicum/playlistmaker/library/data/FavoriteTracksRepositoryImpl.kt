package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.Utils.toTrackEntity
import com.practicum.playlistmaker.Utils.toTracksDomain
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.model.Tracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(private val appDatabase: AppDatabase) :
    FavoriteTracksRepository {

    override suspend fun addTrackToFavorites(track: Tracks) {
        appDatabase.trackDao().insertTrack(track.toTrackEntity())
    }

    override suspend fun removeTrackFromFavorites(track: Tracks) {
        appDatabase.trackDao().deleteTrack(track.toTrackEntity())
    }

    override fun getAllFavoriteTracks(): Flow<List<Tracks>> = flow {
        val tracks = appDatabase.trackDao().getTracks().map { it.toTracksDomain() }
        emit(tracks)
    }

    override fun isTrackFavorite(trackId: Int): Flow<Boolean> = flow {
        emit(appDatabase.trackDao().isTrackFavorite(trackId))
    }
}