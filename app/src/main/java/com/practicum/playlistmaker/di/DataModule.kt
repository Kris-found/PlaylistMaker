package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.application.App.Companion.THEME_SWITCHER_PREFERENCES
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.network.ITunesSearchAPI
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClientImpl
import com.practicum.playlistmaker.search.history.data.SEARCH_HISTORY_PREFERENCES
import com.practicum.playlistmaker.search.history.data.SearchHistoryRepository
import com.practicum.playlistmaker.search.history.data.SearchHistoryRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClientImpl(get(), androidContext())
    }

    single<ITunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchAPI::class.java)
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(named("search_history_prefs")))
    }

    factory { Gson() }

    single(named("search_history_prefs")) {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    single(named("theme_switcher_prefs")) {
        androidContext()
            .getSharedPreferences(THEME_SWITCHER_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.playlistmaker")
            .fallbackToDestructiveMigration()
            .build()
    }
}