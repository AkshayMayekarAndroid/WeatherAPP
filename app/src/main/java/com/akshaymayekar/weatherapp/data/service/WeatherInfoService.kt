package com.akshaymayekar.weatherapp.data.service

import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInfoService {

    @GET("/data/2.5/weather?")
    suspend fun getWeatherInformation(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("appid") api_key : String,
    ) : WeatherInfo
}