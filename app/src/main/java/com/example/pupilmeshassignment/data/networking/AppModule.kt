package com.example.pupilmeshassignment.data.networking

import androidx.compose.animation.core.rememberTransition

object AppModule {

    fun provideApiService(): ApiService{
        return RetrofitClient.apiService
    }

}