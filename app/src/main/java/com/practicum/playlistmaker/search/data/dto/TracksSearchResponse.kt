package com.practicum.playlistmaker.search.data.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TracksDto>
) : Response()