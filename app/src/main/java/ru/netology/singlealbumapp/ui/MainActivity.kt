package ru.netology.singlealbumapp.ui

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.internal.notify
import ru.netology.singlealbumapp.MediaLifecycleObserver
import ru.netology.singlealbumapp.adapter.OnInteractionListener
import ru.netology.singlealbumapp.adapter.TrackAdapter
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import ru.netology.singlealbumapp.dto.Track
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

        val adapter = TrackAdapter(object : OnInteractionListener {

            override fun pauseAll() {
                viewModel.stopPlayingAll()
                binding.list.adapter?.notifyDataSetChanged()
            }

            override fun onPause(tracks: Track) {
                viewModel.stopPlayingAll()
                binding.pause.visibility = View.GONE
                binding.play.visibility = View.VISIBLE
                mediaObserver.apply {
                    player?.pause()
                    player?.reset()

                }

            }

            override fun onPlay(tracks: Track) {
                viewModel.stopPlayingAll()
                mediaObserver.player?.reset()
                binding.pause.visibility = View.VISIBLE
                binding.play.visibility = View.GONE
                viewModel.playTrack(tracks)
                mediaObserver.apply {
                    var songToPlay = tracks.file

                    player?.setDataSource(SONG_URL + songToPlay)

                    player?.setOnCompletionListener {
                        if (tracks.id < 16) {

                            player?.reset()
                            val nextSongId = tracks.id.toInt()
                            var nextTrack = viewModel.albumLiveData().value?.tracks?.get(nextSongId)
                            player?.setDataSource(
                                SONG_URL + nextTrack?.file.toString()
                            )
                            if (nextTrack != null) {
                                viewModel.playTrack(nextTrack)
                            }
                            mediaObserver.play()
                        } else {
                            player?.reset()
                            val backToBeginning = viewModel.albumLiveData().value?.tracks?.get(0)
                            songToPlay = backToBeginning?.file.toString()
                            if (backToBeginning != null) {
                                viewModel.playTrack(backToBeginning)
                            }
                            player?.setDataSource(SONG_URL + songToPlay)
                        }
                    }

                }.play()

            }
        })

        binding.pause.setOnClickListener {
            adapter.notifyDataSetChanged()
            viewModel.stopPlayingAll()
            mediaObserver.player?.pause()
            binding.pause.visibility = View.GONE
            binding.play.visibility = View.VISIBLE
        }

        binding.play.setOnClickListener {
            adapter.notifyDataSetChanged()
            viewModel.stopPlayingAll()
            mediaObserver.apply {
                player?.reset()
                val backToBeginning = viewModel.albumLiveData().value?.tracks?.get(0)
                var songToPlay = backToBeginning?.file.toString()
                player?.setDataSource(SONG_URL + songToPlay)
                if (backToBeginning != null) {
                    viewModel.playTrack(backToBeginning)
                }
            }.play()
            binding.pause.visibility = View.VISIBLE
            binding.play.visibility = View.GONE
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = manager

        if (checkForInternet(applicationContext)){
            viewModel.albumLiveData().observe(this) {
                adapter.submitList(it.tracks)
                adapter.notifyDataSetChanged()
                binding.genre.text = it.genre
                binding.artistName.text = it.artist
                binding.year.text = it.published
                binding.albumTitle.text = it.title
            }
        } else {
            val text = "No internet connection"
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()

        }


//        adapter.submitList(viewModel.data.tracks)
//        adapter.notifyDataSetChanged()
//        binding.genre.text = viewModel.data.genre
//        binding.artistName.text = viewModel.data.artist
//        binding.year.text = viewModel.data.published
//        binding.albumTitle.text = viewModel.data.title

        setContentView(binding.root)
    }


    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}