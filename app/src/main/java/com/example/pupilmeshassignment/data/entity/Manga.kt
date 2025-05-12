package com.example.pupilmeshassignment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "mangas")
data class Manga(
    @PrimaryKey
    val id: String,
    val title: String?,
    val subTitle: String?,
    val status: String?,
    val thumb: String?,
    val summary: String?,
    val authors: String?,  //Storing as JSON String
    val genres: String?,   //Storing as JSON String
    val nsfw: Boolean?,
    val type: String?,
    val totalChapters: Int?,
    val createdAt: Long?,
    val updatedAt: Long?,
    val lastRefreshed: Long = System.currentTimeMillis(),
    val pageNumber: Int?
)

class Converters() {
    private val gson = Gson()

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        val typeToken = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, typeToken)
    }
}

