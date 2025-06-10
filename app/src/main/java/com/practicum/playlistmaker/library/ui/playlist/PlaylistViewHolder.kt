package com.practicum.playlistmaker.library.ui.playlist

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.formattedTracksCount
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.library.domain.playlist.model.Playlist

class PlaylistViewHolder(private val binding: PlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {

        binding.tvTitle.text = item.name
        binding.tvTracksCount.text = formattedTracksCount(item.tracksCount)

        if (item.imageUri.isNullOrBlank()) {
            binding.ivCover.scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            binding.ivCover.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        Glide.with(itemView)
            .load(item.imageUri)
            .placeholder(R.drawable.placeholder_104px)
            .error(R.drawable.placeholder_104px)
            .into(binding.ivCover)
    }
}