package com.example.pupilmeshassignment.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_datastore")

    private val isUserSignedIn = booleanPreferencesKey("isUserSignedIn")

    companion object {
        @Volatile
        var Instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return Instance ?: synchronized(this) {
                Instance ?: DataStoreManager(context.applicationContext).also {
                    Instance = it
                }
            }
        }
    }

    suspend fun setUserSignedIn(isSignedIn: Boolean) {
        context.dataStore.edit { pref ->
            pref[isUserSignedIn] = isSignedIn
        }
    }

    val getUserSignedIn: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[isUserSignedIn] ?: false
    }
}