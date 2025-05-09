package com.example.pupilmeshassignment

import android.app.Application
import com.example.pupilmeshassignment.data.AppContainer

class MyApp() : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}