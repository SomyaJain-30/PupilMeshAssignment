package com.example.pupilmeshassignment.data

import android.content.Context
import com.example.pupilmeshassignment.data.repository.OfflineUserRepository
import com.example.pupilmeshassignment.data.repository.UserRepository

class AppContainer(private val context: Context) {

    val userRepository: UserRepository by lazy {
        OfflineUserRepository(MyDatabase.getDatabase(context).userDao())
    }
}