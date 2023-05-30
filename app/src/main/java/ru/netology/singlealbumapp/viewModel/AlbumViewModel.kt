package ru.netology.singlealbumapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.singlealbumapp.dto.Album
import ru.netology.singlealbumapp.dto.Track
import ru.netology.singlealbumapp.repository.AlbumRepository
import ru.netology.singlealbumapp.repository.AlbumRepositoryImpl


class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AlbumRepository = AlbumRepositoryImpl()

    init {
        loadSongFile()
    }

    private val data = MutableLiveData<Album>()

    fun albumLiveData(): LiveData<Album> {
        return data
    }

    private fun loadSongFile() = viewModelScope.launch {
        repository.getAlbum()
        data.value = repository.getAlbum()
    }


    fun stopPlayingAll() {
//        data.value?.tracks?.forEach {
//            it.isPlaying = false
//        }
        data.value?.tracks?.forEach {
            it.isPlaying = false
        }
    }

    fun playTrack(tracks: Track) {
        data.value?.tracks?.apply {
            tracks.isPlaying = true
        }
    }
}