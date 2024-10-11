package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class TrackAdapter(private val track: ArrayList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return track.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])
    }
}