package com.example.pupilmeshassignment.data.repository

import android.util.Log
import com.example.pupilmeshassignment.data.dao.MangaDao
import com.example.pupilmeshassignment.data.entity.Manga
import com.example.pupilmeshassignment.data.entity.MangaResponse
import com.example.pupilmeshassignment.data.networking.ApiService
import com.example.pupilmeshassignment.data.networking.NetworkCallResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class MangaRepositoryImpl(
    private val apiService: ApiService,
    private val mangaDao: MangaDao
) : MangaRepository {

    private fun MangaResponse.MangaDto.toEntity(page: Int): Manga {
        val gson = Gson()

        return Manga(
            id = id,
            title = title,
            subTitle = subTitle,
            status = status,
            thumb = thumb,
            summary = summary,
            authors = gson.toJson(authors),
            genres = gson.toJson(genres),
            nsfw = nsfw,
            type = type,
            totalChapters = totalChapter,
            createdAt = createAt,
            updatedAt = updateAt,
            pageNumber = page

        )
    }

    private fun Manga.toDomain(): MangaResponse.MangaDto {
        val gson = Gson()
        val authorListType = object : TypeToken<List<String>>() {}.type
        val genreListType = object : TypeToken<List<String>>() {}.type

        return MangaResponse.MangaDto(
            authors = gson.fromJson(authors, authorListType),
            createAt = createdAt,
            genres = gson.fromJson(genres, genreListType),
            id = id,
            nsfw = nsfw,
            status = status,
            subTitle = subTitle,
            summary = summary,
            thumb = thumb,
            title = title,
            totalChapter = totalChapters,
            type = type,
            updateAt = updatedAt

        )
    }

    override suspend fun getMangas(
        page: Int,
        forceRefresh: Boolean
    ): Flow<NetworkCallResponse<List<MangaResponse.MangaDto>>> {
        return flow {
            Log.e("CHECK-->", "0")
            emit(NetworkCallResponse.Loading())
            Log.e("CHECK-->", "1")
            //check cache first if not forcing refresh
            if (!forceRefresh) {
                /*mangaDao.getMangasByPage(page).collectLatest { mangas ->
                    if (mangas.isNotEmpty()) {
                        Log.e("CHECK-->", "2: $mangas")
                        emit(NetworkCallResponse.Success(mangas.map { it.toDomain() }))
                        return@collectLatest
                    }
                }*/

                val cachedMangas = mangaDao.getMangasByPage(page).firstOrNull()
                if (!cachedMangas.isNullOrEmpty()) {
                    Log.e("CHECK-->", "2: $cachedMangas")
                    emit(NetworkCallResponse.Success(cachedMangas.map { it.toDomain() }))
                    return@flow
                }
            }

            //if cache is empty or force refresh, fetch from network
            try {
                val response = apiService.fetchMangas(page)
                if (response.code == 200) {
                    Log.e("CHECK-->", "3: ${response.data}")
                    //clear previous data for this page if exist
                    mangaDao.clearPageCache(page)

                    //save new data to cache
                    val mangas = response.data?.map { it.toEntity(page) }
                    mangaDao.insertMangas(mangas ?: emptyList())

                    //emit fresh data from database to ensure consistency
                    mangaDao.getMangasByPage(page).collectLatest { freshMangas ->
                        emit(NetworkCallResponse.Success(freshMangas.map { it.toDomain() }))
                    }
                } else {
                    Log.e("CHECK-->", "4: ${response.data}")
                    emit(NetworkCallResponse.Error("API Error: ${response.code}"))
                }
            } catch (e: Exception) {
                emit(NetworkCallResponse.Error("Network Error: ${e.message}"))
                Log.e("CHECK-->", "5 ${e.message}}")
                //try to return cached data
                /*mangaDao.getMangasByPage(page).collectLatest { mangas ->
                    if (mangas.isNotEmpty()) {
                        Log.e("CHECK-->", "6: $mangas")
                        emit(
                            NetworkCallResponse.Success(
                                mangas.map { it.toDomain() },
                                isCached = true
                            )
                        )
                    }
                }*/
                val fallbackMangas = mangaDao.getMangasByPage(page).firstOrNull()
                if (!fallbackMangas.isNullOrEmpty()) {
                    Log.e("CHECK-->", "6: $fallbackMangas")
                    emit(
                        NetworkCallResponse.Success(
                            fallbackMangas.map { it.toDomain() },
                            isCached = true
                        )
                    )
                }
            }

        }
    }

    override suspend fun getMangaById(id: String): Flow<NetworkCallResponse<MangaResponse.MangaDto>> {
        return flow {
            emit(NetworkCallResponse.Loading())

            try {
                val manga = mangaDao.getMangaById(id)
                if (manga != null) {
                    emit(NetworkCallResponse.Success(manga.toDomain()))
                } else {
                    emit(NetworkCallResponse.Error("Manga not found"))
                }
            } catch (e: Exception) {
                emit(NetworkCallResponse.Error("Database Error: ${e.message}"))
            }
        }
    }
}