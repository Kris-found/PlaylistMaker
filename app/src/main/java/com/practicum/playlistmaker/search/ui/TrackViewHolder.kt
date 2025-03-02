package com.practicum.playlistmaker.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Utils.dpToPx
import com.practicum.playlistmaker.databinding.SearchTrackViewBinding
import com.practicum.playlistmaker.search.domain.model.Tracks

class TrackViewHolder(private val binding: SearchTrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Tracks) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2.0f, this.itemView.context)))
            .into(binding.ivCoverTrack)

        binding.tvTrackName.text = model.trackName
        binding.tvArtistName.text = model.artistName
        binding.tvTrackDuration.text = model.formattedTrackTime
        binding.tvArtistName.requestLayout()
        binding.tvTrackDuration.requestLayout()
    }
}