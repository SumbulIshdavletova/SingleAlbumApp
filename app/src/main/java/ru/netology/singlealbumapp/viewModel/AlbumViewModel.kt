package ru.netology.singlealbumapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.singlealbumapp.dto.Tracks
import ru.netology.singlealbumapp.repository.AlbumRepository
import ru.netology.singlealbumapp.repository.AlbumRepositoryImpl


class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AlbumRepository = AlbumRepositoryImpl()

    init {
        loadSongFile()
    }

    val data = repository.getAllFlow().asLiveData()

    private fun loadSongFile() = viewModelScope.launch {
        repository.getAllFlow().collect()
    }


    fun stopPlayingAll() {
        data.value?.tracks?.forEach {
            val i = arrayOf(it)
                for (t in i) {
                    t.isPlaying = false
                }

        }
    }

    fun isPlaying(tracks: Tracks) {
        data.value?.tracks?.apply {
            tracks.isPlaying = true
        }
    }
}