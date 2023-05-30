package ru.netology.singlealbumapp.repository

import ru.netology.singlealbumapp.api.AlbumApi
import ru.netology.singlealbumapp.dto.Album
import ru.netology.singlealbumapp.error.ApiError
import ru.netology.singlealbumapp.error.NetworkError
import ru.netology.singlealbumapp.error.UnknownError
import java.io.IOException

class AlbumRepositoryImpl() : AlbumRepository {

    override suspend fun getAlbum(): Album {
        var album: Album
        try {
            val response = AlbumApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            album = body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
        return album
    }
}


