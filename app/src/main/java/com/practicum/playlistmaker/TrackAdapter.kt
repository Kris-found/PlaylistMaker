package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class TrackAdapter(private var track: ArrayList<Track>, private val clickListener: ClickListener): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return track.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(track[position])

        holder.itemView.setOnClickListener{
            val track = track[position]
            clickListener.onItemClick(track)
        }
    }

    fun updateData(track: ArrayList<Track>){
        this.track = track
        notifyDataSetChanged()
    }


    fun interface ClickListener{
        fun onItemClick(track: Track)
    }
}
