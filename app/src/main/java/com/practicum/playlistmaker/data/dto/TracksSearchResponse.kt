package com.practicum.playlistmaker.data.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TracksDto>
) : Response()