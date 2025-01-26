package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val TRACK_HISTORY_KEY = "key_for_history_search"
const val SEARCH_HISTORY_PREFERENCES = "search_history"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun getHistoryTrack():ArrayList<Track>{
        val json = sharedPreferences.getString(TRACK_HISTORY_KEY, null) ?: return ArrayList()
        val array = Gson().fromJson(json, Array<Track>::class.java)
        return ArrayList(array.toList())
    }

    fun clearHistory(){
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    fun saveTrackToHistory(tracks: ArrayList<Track>){
        sharedPreferences.edit()
            .putString(TRACK_HISTORY_KEY, Gson().toJson(tracks))
            .apply()
    }

    fun addTrackToHistory(track: Track){
        val history = getHistoryTrack().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > MAX_LIMIT_SONGS){
            history.removeAt(history.lastIndex)
        }

        saveTrackToHistory(ArrayList(history))
    }

    companion object {
        const val MAX_LIMIT_SONGS = 10
    }
}