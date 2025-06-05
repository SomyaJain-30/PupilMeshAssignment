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
            when (val response = repository.getMangas(pageNumber).first()) {
                is NetworkCallResponse.Success -> {
                    val mangas = response.data ?: emptyList()

                    LoadResult.Page(
                        data = mangas,
                        prevKey = if (pageNumber > 1) pageNumber.minus(1) else null,
                        nextKey = if (mangas.isNotEmpty()) pageNumber.plus(1) else null
                    )
                }

                is NetworkCallResponse.Error -> {
                    LoadResult.Error(Exception(response.message))
                }

                else -> {
                    LoadResult.Error(Exception("Unknown Error"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}