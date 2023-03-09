package ru.netology.singlealbumapp.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.netology.singlealbumapp.api.AlbumApi
import ru.netology.singlealbumapp.dto.Album
import ru.netology.singlealbumapp.error.ApiError
import ru.netology.singlealbumapp.error.NetworkError
import ru.netology.singlealbumapp.error.UnknownError
import java.io.IOException

class AlbumRepositoryImpl() : AlbumRepository {

    override fun getAllFlow(): Flow<Album> = flow {

        try {
            val response = AlbumApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            emit(body)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}


