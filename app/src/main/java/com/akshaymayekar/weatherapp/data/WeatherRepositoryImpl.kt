package com.akshaymayekar.weatherapp.data

import com.akshaymayekar.weatherapp.data.service.WeatherInfoService
import com.akshaymayekar.weatherapp.domain.WeatherRepository
import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private var weatherInfoService: WeatherInfoService,
    private val apiKey: String,
) : WeatherRepository {

    override fun getWeatherInformation(lat: Double, long: Double): Flow<Response<WeatherInfo>> =
        flow {
            try {
                emit(Response.Loading)
                val responseApi = weatherInfoService.getWeatherInformation(
                    lat = lat,
                    lon = long,
                    api_key = apiKey
                )
                emit(Response.Success(responseApi))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
}