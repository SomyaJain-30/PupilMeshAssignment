package com.example.pupilmeshassignment.data.networking

import com.example.pupilmeshassignment.data.entity.MangaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("manga/fetch")
    suspend fun fetchMangas(
        @Query("page") page: Int
    ): MangaResponse
}