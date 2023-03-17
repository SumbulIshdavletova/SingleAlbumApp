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
    file = ""
)

private val mediaObserver = MediaLifecycleObserver()

interface OnInteractionListener {
    fun onPlay(tracks: Tracks) {}
    fun onPause(tracks: Tracks) {}
}

class TrackAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Tracks, TrackViewHolder>(TrackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = SongsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, onInteractionListener)
    }


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

}

class TrackViewHolder(
    private val binding: SongsCardBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Tracks) {
        binding.apply {
            songTitle.text = track.file

       //     play.isChecked = mediaObserver.player?.isPlaying == true

            play.setOnClickListener {
                pause.visibility = View.VISIBLE
                play.visibility = View.GONE
                onInteractionListener.onPlay(track)
            }

            pause.setOnClickListener {
                pause.visibility = View.GONE
                play.visibility = View.VISIBLE
                onInteractionListener.onPause(track)
            }

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
