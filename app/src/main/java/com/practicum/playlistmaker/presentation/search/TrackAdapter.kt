package com.practicum.playlistmaker.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.model.Tracks
import kotlin.collections.ArrayList

class TrackAdapter(
    private var tracks: ArrayList<Tracks>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            val track = tracks[position]
            clickListener.onItemClick(track)
        }
    }

    fun updateData(track: ArrayList<Tracks>) {
        this.tracks = track
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onItemClick(track: Tracks)
    }
}
