package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SearchTrackViewBinding
import com.practicum.playlistmaker.search.domain.model.Tracks
import com.practicum.playlistmaker.search.ui.TrackAdapter.OnLongClickListener

class TrackAdapter(
    private var tracks: ArrayList<Tracks>,
    private val clickListener: ClickListener,
    private val longClickListener: OnLongClickListener = OnLongClickListener {},
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(SearchTrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.bind(track)

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(track)
        }

        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick(track)
            true
        }
    }

    fun updateData(track: ArrayList<Tracks>) {
        this.tracks = track
        notifyDataSetChanged()
    }

    fun interface ClickListener {
        fun onItemClick(track: Tracks)
    }

    fun interface OnLongClickListener {
        fun onLongClick(track: Tracks)
    }
}
