package com.example.pupilmeshassignment.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pupilmeshassignment.data.entity.MangaResponse
import com.example.pupilmeshassignment.data.networking.NetworkCallResponse
import com.example.pupilmeshassignment.data.repository.MangaRepository
import kotlinx.coroutines.flow.first

class MangaPagingSource(private val repository: MangaRepository) :
    PagingSource<Int, MangaResponse.MangaDto>() {
    override fun getRefreshKey(state: PagingState<Int, MangaResponse.MangaDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MangaResponse.MangaDto> {
        val pageNumber = params.key ?: 1

        return try {
            val response = repository.getMangas(pageNumber).first()
            Log.e("CHECK-->", "Paging: 1")
            Log.e("CHECK-->", "Paging: 1: $response")
            when (response) {
                is NetworkCallResponse.Success -> {
                    val mangas = response.data ?: emptyList()
                    Log.e("CHECK-->", "Paging: 2")

                    LoadResult.Page(
                        data = mangas,
                        prevKey = if (pageNumber > 1) pageNumber.minus(1) else null,
                        nextKey = if (mangas.isNotEmpty()) pageNumber.plus(1) else null
                    )
                }

                is NetworkCallResponse.Error -> {
                    Log.e("CHECK-->", "Paging: 3")
                    LoadResult.Error(Exception(response.message))
                }

                else -> {
                    Log.e("CHECK-->", "Paging: 4")
                    LoadResult.Error(Exception("Unknown Error"))
                }
            }
        } catch (e: Exception) {
            Log.e("CHECK-->", "Paging: 5")
            LoadResult.Error(e)
        }
    }
}