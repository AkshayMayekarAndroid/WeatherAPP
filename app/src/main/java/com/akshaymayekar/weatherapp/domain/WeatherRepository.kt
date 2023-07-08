package com.akshaymayekar.weatherapp.domain

import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherInformation(lat: Double, long:Double): Flow<Response<WeatherInfo>>
}