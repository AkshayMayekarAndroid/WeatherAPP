package com.akshaymayekar.weatherapp.domain

import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import kotlinx.coroutines.flow.Flow

interface WeatherLocalRepository {
    suspend fun getLastWeatherInformation(): Flow<Response<WeatherInfo>>
    suspend fun setWeatherInformation(toString: String)
}