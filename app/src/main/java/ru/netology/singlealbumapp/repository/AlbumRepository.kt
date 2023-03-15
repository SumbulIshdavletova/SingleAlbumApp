package ru.netology.singlealbumapp.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.singlealbumapp.dto.Album

interface AlbumRepository {

   fun getAllFlow(): Flow<Album>
}