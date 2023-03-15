package ru.netology.singlealbumapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbumapp.MediaLifecycleObserver
import ru.netology.singlealbumapp.databinding.SongsCardBinding
import ru.netology.singlealbumapp.dto.Tracks

private val empty = Tracks(
    id = 0,
    file = "",
    isPlaying = false
)

private val mediaObserver = MediaLifecycleObserver()

interface OnInteractionListener1 {
    fun onPlay(tracks: Tracks) {}
    fun onPause(tracks: Tracks) {}
}

class TrackAdapter(
    private val onInteractionListener1: OnInteractionListener1,
) : ListAdapter<Tracks, TrackViewHolder>(TrackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = SongsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, onInteractionListener1)
    }


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

}

class TrackViewHolder(
    private val binding: SongsCardBinding,
    private val onInteractionListener1: OnInteractionListener1,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Tracks) {
        binding.apply {
            songTitle.text = track.file

            play.setOnClickListener {
                onInteractionListener1.onPlay(track)

                checkIsPlaying(track, binding)
            }

            pause.setOnClickListener {
                onInteractionListener1.onPause(track)

                checkIsPlaying(track, binding)
            }

            checkIsPlaying(track, binding)

        }
    }

    private fun checkIsPlaying(track: Tracks, binding: SongsCardBinding) {
        if (track.isPlaying) {
            binding.pause.visibility = View.VISIBLE
            binding.play.visibility = View.GONE
        } else {
            binding.pause.visibility = View.GONE
            binding.play.visibility = View.VISIBLE
        }
    }
}



class TrackDiffCallback : DiffUtil.ItemCallback<Tracks>() {
    override fun areItemsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem == newItem
    }

}
