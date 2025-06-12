package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.Utils.Resource
import com.practicum.playlistmaker.Utils.toDomain
import com.practicum.playlistmaker.Utils.toDto
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.history.data.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val appDatabase: AppDatabase,
) : TracksRepository {

    companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val ERROR = "Error"
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Tracks>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as TracksSearchResponse) {
                    val favoritesTracks = appDatabase.trackDaoFavorite().getTracksId()
                    val data = results.map {
                        it.toDomain()
                    }
                    data.filter {
                        it.trackId in favoritesTracks
                    }.map { it.copy(isFavorite = true) }
                    emit(Resource.Success(data))
                }
            }

            -1 -> {
                emit(Resource.Error(NO_INTERNET_CONNECTION))
            }

            else -> {
                emit(Resource.Error("$ERROR: ${response.resultCode}"))
            }
        }
    }

    override fun saveTrackToHistory(tracks: ArrayList<Tracks>) {
        val dtoTracks = tracks.map { it.toDto() }
        searchHistoryRepository.saveTrackToHistory(ArrayList(dtoTracks))
    }

    override fun getHistoryTrack(): Flow<ArrayList<Tracks>> = flow {
        val dtoTracks = searchHistoryRepository.getHistoryTrack()
        val favoritesTracks = appDatabase.trackDaoFavorite().getTracksId()

        val tracks = dtoTracks.map { it.toDomain() }
        tracks.filter {
            it.trackId in favoritesTracks
        }.map { it.copy(isFavorite = true) }

        emit(ArrayList(tracks))
    }

    override fun addTrackToHistory(track: Tracks) {
        val dtoTrack = track.toDto()
        searchHistoryRepository.addTrackToHistory(dtoTrack)
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}