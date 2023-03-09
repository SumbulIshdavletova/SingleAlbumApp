package ru.netology.singlealbumapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.*
import ru.netology.singlealbumapp.MediaLifecycleObserver
import ru.netology.singlealbumapp.adapter.OnInteractionListener
import ru.netology.singlealbumapp.adapter.TracksAdapter
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import ru.netology.singlealbumapp.dto.Tracks
import ru.netology.singlealbumapp.viewModel.AlbumViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val mediaObserver = MediaLifecycleObserver()
    private val SONG_URL =
        "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val manager = LinearLayoutManager(this)
        val adapter = TracksAdapter(object : OnInteractionListener {
            override fun onPause(track: Tracks) {
                mediaObserver.apply {
                    player?.pause()
                }
            }

            override fun onPlay(track: Tracks) {

                mediaObserver.apply {
                    player?.setDataSource(SONG_URL + track.file)
                }.play()
                mediaObserver.player?.setOnCompletionListener {
                    it.release()
                }
            }
        })


        viewModel.data.observeForever() {
            adapter.data = it.tracks
            binding.genre.text = it.genre
            binding.artistName.text = it.artist
            binding.year.text = it.published
            binding.albumTitle.text = it.title
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = manager

        setContentView(binding.root)
    }


}