package com.example.pupilmeshassignment.data.networking


object AppModule {

    fun provideApiService(): ApiService{
        return RetrofitClient.apiService
    }

}