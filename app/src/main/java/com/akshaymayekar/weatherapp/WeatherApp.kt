package com.akshaymayekar.weatherapp

import android.app.Application
import androidx.room.Room
import com.akshaymayekar.weatherapp.data.localStorage.AppDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class WeatherApp : Application() {

    companion object{
        lateinit var db : AppDatabase
    }
    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "WeatherDB"
        ).build()
    }

}