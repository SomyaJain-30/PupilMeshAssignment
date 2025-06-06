package com.example.pupilmeshassignment.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pupilmeshassignment.data.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email AND (:password IS NULL OR password = :password))")
    suspend fun isValidUser(email: String, password: String?): Boolean
}