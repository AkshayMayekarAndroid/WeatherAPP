package com.akshaymayekar.weatherapp.data.localStorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastWeatherInfo(

    @PrimaryKey val id: Int,
    @ColumnInfo(name = "weather_info") val weatherInfo: String
)