package com.practicum.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.favorite.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.model.FavoriteTracksState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private val favoriteTracksLiveData = MutableLiveData<FavoriteTracksState>()
    fun favoriteTracksState(): LiveData<FavoriteTracksState> = favoriteTracksLiveData

    init {
        viewModelScope.launch {
            getFavoriteTracks()
        }
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            favoriteInteractor.getAllFavoriteTracks().collect {
                if (it.isEmpty()) setState(FavoriteTracksState.Empty) else setState(
                    FavoriteTracksState.Content(it)
                )
            }
        }
    }

    private fun setState(state: FavoriteTracksState) {
        favoriteTracksLiveData.postValue(state)
    }
}