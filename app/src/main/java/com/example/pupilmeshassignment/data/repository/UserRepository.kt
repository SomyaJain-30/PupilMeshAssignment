package com.example.pupilmeshassignment.data.repository

import com.example.pupilmeshassignment.data.entity.User

interface UserRepository {

    suspend fun getUserByEmail(email: String): User?

    suspend fun insertUser(user: User)

    suspend fun isValidUser(email: String, password: String): Boolean
}