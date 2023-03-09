package ru.netology.singlealbumapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.netology.singlealbumapp.BuildConfig
import ru.netology.singlealbumapp.dto.Album


private const val BASE_URL =
    "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface ApiService {
    @GET("album.json")
    suspend fun getAll(): Response<Album>
}

object AlbumApi {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}