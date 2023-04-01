package ru.netology.singlealbumapp.adapter

import android.database.Observable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import ru.netology.singlealbumapp.MediaLifecycleObserver
import ru.netology.singlealbumapp.R
import ru.netology.singlealbumapp.databinding.SongsCardBinding
import ru.netology.singlealbumapp.dto.Tracks


interface OnInteractionListener {
    fun onPlay(tracks: Tracks) {}
    fun onPause(tracks: Tracks) {}
    fun pauseAll() {}
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

            if (track.isPlaying) {
                pause.visibility = View.VISIBLE
                play.visibility = View.GONE
                pause.setOnClickListener {
                    onInteractionListener.onPause(track)
                    pause.visibility = View.GONE
                    play.visibility = View.VISIBLE
                }
            } else {
                pause.visibility = View.GONE
                play.visibility = View.VISIBLE
                play.setOnClickListener {

                   onInteractionListener.pauseAll()
                    onInteractionListener.onPlay(track)
                    pause.visibility = View.VISIBLE
                    play.visibility = View.GONE
                }
            }

            pause.setOnClickListener {
                onInteractionListener.onPause(track)
                onInteractionListener.pauseAll()
                binding.pause.visibility = View.GONE
                binding.play.visibility = View.VISIBLE
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
