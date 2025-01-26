package com.practicum.playlistmaker.data.impl

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.gson.Gson
import com.practicum.playlistmaker.data.dto.TracksDto
import com.practicum.playlistmaker.data.repository.SearchHistoryRepository

const val TRACK_HISTORY_KEY = "key_for_history_search"
const val SEARCH_HISTORY_PREFERENCES = "search_history"

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {

    companion object {
        const val MAX_LIMIT_SONGS = 10
    }

    override fun saveTrackToHistory(tracks: ArrayList<TracksDto>) {
        sharedPreferences.edit()
            .putString(TRACK_HISTORY_KEY, Gson().toJson(tracks))
            .apply()
    }

    override fun getHistoryTrack(): ArrayList<TracksDto> {
        val json = sharedPreferences.getString(TRACK_HISTORY_KEY, null) ?: return ArrayList()
        val array = Gson().fromJson(json, Array<TracksDto>::class.java)
        return ArrayList(array.toList())
    }

    override fun addTrackToHistory(track: TracksDto) {
        val history = getHistoryTrack().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > MAX_LIMIT_SONGS) {
            history.removeAt(history.lastIndex)
        }

        saveTrackToHistory(ArrayList(history))
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    override fun registerChangeListener(onChange: () -> Unit) {
        val listener = OnSharedPreferenceChangeListener { _, key ->
            if (key == TRACK_HISTORY_KEY) {
                onChange()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
}