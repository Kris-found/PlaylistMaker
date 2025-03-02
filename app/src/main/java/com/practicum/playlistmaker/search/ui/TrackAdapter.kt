package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SearchTrackViewBinding
import com.practicum.playlistmaker.search.domain.model.Tracks

class TrackAdapter(
    private var tracks: ArrayList<Tracks>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(SearchTrackViewBinding.inflate(layoutInspector, parent, false))
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
