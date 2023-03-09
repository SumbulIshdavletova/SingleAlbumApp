package ru.netology.singlealbumapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbumapp.databinding.SongsCardBinding
import ru.netology.singlealbumapp.dto.Tracks


interface OnInteractionListener {
    fun onPlay(track: Tracks) {}
    fun onPause(track: Tracks) {}
}

class TracksAdapter(
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.Adapter<TracksAdapter.TracksViewHolder>() {

    var data: List<Tracks> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class TracksViewHolder(
        val binding: SongsCardBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Tracks) {
            binding.apply {
                songTitle
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val binding = SongsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TracksViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        val track = data[position]
        val context = holder.itemView.context
        with(holder.binding) {
            songTitle.text = track.file

            play.setOnClickListener {
                onInteractionListener.onPlay(track)
                pause.visibility = View.VISIBLE
                play.visibility = View.GONE
                //    onInteractionListener.onPause(track)
            }

            pause.setOnClickListener {
                pause.visibility = View.GONE
              //  onInteractionListener.onPlay(track)
                onInteractionListener.onPause(track)
                play.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = data.size
}

class TracksDiffCallback : DiffUtil.ItemCallback<Tracks>() {
    override fun areItemsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tracks, newItem: Tracks): Boolean {
        return oldItem == newItem
    }

}