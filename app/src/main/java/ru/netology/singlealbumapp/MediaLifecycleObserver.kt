package ru.netology.singlealbumapp

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.netology.singlealbumapp.dto.Tracks
import java.lang.String
import java.net.URL
import kotlin.Boolean
import kotlin.Exception
import kotlin.Int
import kotlin.Throws
import kotlin.Unit

class MediaLifecycleObserver : LifecycleEventObserver {
    var player: MediaPlayer? = MediaPlayer()
    var tracks: Tracks = Tracks(id = 0, file = "", isPlaying = false)

    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepareAsync()
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> player?.pause()
            Lifecycle.Event.ON_STOP -> {
                player?.release()
                player = null
            }
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}