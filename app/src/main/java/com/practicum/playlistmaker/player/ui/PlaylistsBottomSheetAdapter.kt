package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.practicum.playlistmaker.databinding.PlaylistViewBottomSheetBinding
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist

class PlaylistsBottomSheetAdapter(private val clickListener: PlaylistClickListener) :
    ListAdapter<Playlist, PlaylistsBottomSheetViewHolder>(
        object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }

        }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsBottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsBottomSheetViewHolder(
            PlaylistViewBottomSheetBinding.inflate(
                layoutInspector, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistsBottomSheetViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}