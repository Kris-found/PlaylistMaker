package com.practicum.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.dpToPx
import com.practicum.playlistmaker.Utils.formattedTracksCount
import com.practicum.playlistmaker.databinding.PlaylistViewBottomSheetBinding
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist

class PlaylistsBottomSheetViewHolder(private val binding: PlaylistViewBottomSheetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist, clickListener: PlaylistsBottomSheetAdapter.PlaylistClickListener) {
        binding.tvTitle.text = item.name
        binding.countTracks.text = formattedTracksCount(item.tracksCount)

        binding.root.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                clickListener.onPlaylistClick(item)
            }
        }

        Glide.with(itemView)
            .load(item.imageUri)
            .transform(CenterCrop(), RoundedCorners(dpToPx(2.0f, this.itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(binding.ivCover)
    }
}