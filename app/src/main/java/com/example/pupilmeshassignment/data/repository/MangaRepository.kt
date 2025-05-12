package com.example.pupilmeshassignment.data.repository

import com.example.pupilmeshassignment.data.entity.MangaResponse
import com.example.pupilmeshassignment.data.networking.NetworkCallResponse
import kotlinx.coroutines.flow.Flow

interface MangaRepository {

    suspend fun getMangas(page: Int, forceRefresh: Boolean = false): Flow<NetworkCallResponse<List<MangaResponse.MangaDto>>>

    suspend fun getMangaById(id: String): Flow<NetworkCallResponse<MangaResponse.MangaDto>>
}