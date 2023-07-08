package com.akshaymayekar.weatherapp.data.localStorage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LastWeatherInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun WeatherDao(): WeatherDao
}