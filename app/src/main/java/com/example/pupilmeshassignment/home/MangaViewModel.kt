package com.example.pupilmeshassignment.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pupilmeshassignment.data.entity.MangaResponse
import com.example.pupilmeshassignment.data.networking.NetworkCallResponse
import com.example.pupilmeshassignment.data.paging.MangaPagingSource
import com.example.pupilmeshassignment.data.repository.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MangaViewModel(
    private val mangaRepository: MangaRepository
) : ViewModel() {

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _mangas = MutableStateFlow<PagingData<MangaResponse.MangaDto>>(PagingData.empty())
    val mangas: StateFlow<PagingData<MangaResponse.MangaDto>> = _mangas.asStateFlow()

    private val _selectedManga = MutableStateFlow<MangaResponse.MangaDto?>(null)
    val selectedManga: StateFlow<MangaResponse.MangaDto?> = _selectedManga.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        initPaging()
    }

    private fun initPaging() {
        Log.e("CHECK-->", "initPaging: 1")
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                    prefetchDistance = 2
                ),
                pagingSourceFactory = {
                    MangaPagingSource(mangaRepository)
                }
            ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                Log.e("CHECK-->", "initPaging: 2 ${pagingData}")
                _mangas.value = pagingData
            }
        }
    }

    fun loadNextPage() {
        _currentPage.value += 1
    }

    fun refreshData() {
        initPaging()
    }

    fun selectManga(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            mangaRepository.getMangaById(id).collectLatest { result ->
                when (result) {
                    is NetworkCallResponse.Success -> {
                        _selectedManga.value = result.data
                        _isLoading.value = false
                    }

                    is NetworkCallResponse.Error -> {
                        _errorMessage.value = result.message
                        _isLoading.value = false
                    }

                    is NetworkCallResponse.Loading -> {
                        _isLoading.value = true
                    }

                }
            }
        }
    }

    fun clearSelection() {
        _selectedManga.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

}

class MangaViewModelFactory(private val mangaRepository: MangaRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MangaViewModel::class.java)) {
            return MangaViewModel(mangaRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}