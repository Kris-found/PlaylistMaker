package com.practicum.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Tracks
import com.practicum.playlistmaker.Utils.dpToPx

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.search_track_view, parent, false)
    ) {

    private val ivCoverTrack = itemView.findViewById<ImageView>(R.id.ivCoverTrack)
    private val tvTrackName = itemView.findViewById<TextView>(R.id.tvTrackName)
    private val tvArtistName = itemView.findViewById<TextView>(R.id.tvArtistName)
    private val tvTrackTime = itemView.findViewById<TextView>(R.id.tvTrackDuration)

    fun bind(model: Tracks) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2.0f, this.itemView.context)))
            .into(ivCoverTrack)

        tvTrackName.text = model.trackName
        tvArtistName.text = model.artistName
        tvTrackTime.text = model.formattedTrackTime
        tvArtistName.requestLayout()
        tvTrackTime.requestLayout()
    }
}