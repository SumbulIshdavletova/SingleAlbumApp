package ru.netology.singlealbumapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
}