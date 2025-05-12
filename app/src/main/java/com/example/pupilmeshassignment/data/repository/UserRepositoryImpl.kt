package com.example.pupilmeshassignment.data.repository

import com.example.pupilmeshassignment.data.dao.UserDao
import com.example.pupilmeshassignment.data.entity.User

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun isValidUser(email: String, password: String?): Boolean =
        userDao.isValidUser(email, password)
}