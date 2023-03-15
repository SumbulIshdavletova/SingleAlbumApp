package ru.netology.singlealbumapp.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.singlealbumapp.MediaLifecycleObserver
import ru.netology.singlealbumapp.adapter.OnInteractionListener1
import ru.netology.singlealbumapp.adapter.TrackAdapter
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import ru.netology.singlealbumapp.dto.Tracks
import ru.netology.singlealbumapp.viewModel.AlbumViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val mediaObserver = MediaLifecycleObserver()
    private val SONG_URL =
        "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    var player: MediaPlayer? = MediaPlayer()
    var playingPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val manager = LinearLayoutManager(this)

        val adapter = TrackAdapter(object : OnInteractionListener1 {

            override fun onPause(tracks: Tracks) {
                viewModel.changePlayingStatus(tracks, false)
                binding.pause.visibility = View.GONE
                binding.play.visibility = View.VISIBLE
                mediaObserver.apply {
                    player?.pause()
                    player?.reset()

                }

            }

            override fun onPlay(tracks: Tracks) {

                mediaObserver.player?.reset()
                binding.pause.visibility = View.VISIBLE
                binding.play.visibility = View.GONE

                mediaObserver.apply {
                    var songToPlay = tracks.file
                    player?.setDataSource(SONG_URL + songToPlay)
                    viewModel.changePlayingStatus(tracks, true)

                    player?.setOnCompletionListener {

                        if (tracks.id < 16) {
                            this.tracks.isPlaying = false
                            player?.reset()
                            player?.setDataSource(
                                SONG_URL + (viewModel.data.value?.tracks?.get(
                                    tracks.id.toInt()+1
                                ))?.file.toString()
                            )
                            mediaObserver.play()
                        } else {
                            player?.reset()
                            val backToBeginning = viewModel.data.value?.tracks?.get(0)
                            songToPlay = backToBeginning?.file.toString()
                            player?.setDataSource(SONG_URL + songToPlay)
                        }
                    }

                }.play()

            }
        })

        binding.pause.setOnClickListener {
            mediaObserver.player?.pause()
            mediaObserver.player?.currentPosition
            binding.pause.visibility = View.GONE
            binding.play.visibility = View.VISIBLE
        }

        binding.play.setOnClickListener {
            mediaObserver.apply {
                player?.reset()
                val backToBeginning = viewModel.data.value?.tracks?.get(0)
                var songToPlay = backToBeginning?.file.toString()
                player?.setDataSource(SONG_URL + songToPlay)
                viewModel.changePlayingStatus(tracks, true)

            }.play()
            binding.pause.visibility = View.VISIBLE
            binding.play.visibility = View.GONE
        }

        viewModel.data.observeForever()
        {
            adapter.submitList(it.tracks)
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