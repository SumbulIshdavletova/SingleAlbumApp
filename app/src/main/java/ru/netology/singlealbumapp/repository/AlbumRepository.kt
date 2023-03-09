package ru.netology.singlealbumapp.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.singlealbumapp.dto.Album

interface AlbumRepository {

   fun getAllFlow(): Flow<Album>
}