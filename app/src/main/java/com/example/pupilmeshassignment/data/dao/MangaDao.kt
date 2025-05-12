package com.example.pupilmeshassignment.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pupilmeshassignment.data.entity.Manga
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(mangas: List<Manga>)

    @Query("SELECT * FROM mangas WHERE pageNumber = :page")
    fun getMangasByPage(page: Int): Flow<List<Manga>>

    @Query("SELECT * FROM mangas WHERE id = :id")
    suspend fun getMangaById(id: String): Manga?

    @Query("SELECT MAX(pageNumber) FROM mangas")
    suspend fun getLastCachedPage(): Int?

    @Query("SELECT * FROM mangas")
    fun getAllMangas(): Flow<List<Manga>>

    @Query("DELETE FROM mangas WHERE pageNumber = :page")
    suspend fun clearPageCache(page: Int)
}