package com.practicum.playlistmaker.library.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist

class PlaylistAdapter(private val clickListener: OnClickListener) :
    ListAdapter<Playlist, PlaylistViewHolder>(
        object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { clickListener.onItemClick(getItem(position)) }

    }

    fun interface OnClickListener {
        fun onItemClick(playlist: Playlist)
    }
}