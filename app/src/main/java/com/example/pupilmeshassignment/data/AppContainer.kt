package com.example.pupilmeshassignment.data

import android.content.Context
import com.example.pupilmeshassignment.data.networking.AppModule
import com.example.pupilmeshassignment.data.repository.MangaRepository
import com.example.pupilmeshassignment.data.repository.MangaRepositoryImpl
import com.example.pupilmeshassignment.data.repository.UserRepositoryImpl
import com.example.pupilmeshassignment.data.repository.UserRepository

class AppContainer(private val context: Context) {

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(MyDatabase.getDatabase(context).userDao())
    }

    val mangaRepository: MangaRepository by lazy {
        MangaRepositoryImpl(
            AppModule.provideApiService(),
            MyDatabase.getDatabase(context).mangaDao()
        )
    }
}