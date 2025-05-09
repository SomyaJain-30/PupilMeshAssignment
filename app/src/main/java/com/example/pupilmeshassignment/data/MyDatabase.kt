package com.example.pupilmeshassignment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pupilmeshassignment.data.dao.UserDao
import com.example.pupilmeshassignment.data.entity.User
import kotlin.concurrent.Volatile

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyDatabase::class.java, "zen_database")
                    .build().also {
                        Instance = it
                    }
            }
        }
    }
}