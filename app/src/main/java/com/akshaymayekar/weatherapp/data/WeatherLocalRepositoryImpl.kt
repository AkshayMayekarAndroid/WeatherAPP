package com.akshaymayekar.weatherapp.data

import com.akshaymayekar.weatherapp.WeatherApp
import com.akshaymayekar.weatherapp.data.localStorage.LastWeatherInfo
import com.akshaymayekar.weatherapp.domain.WeatherLocalRepository
import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalRepositoryImpl @Inject constructor(

) : WeatherLocalRepository {
    /* override fun getWeatherInformation(lat: Double, long: Double): Flow<Response<WeatherInfo>> {
         TODO("Not yet implemented")
     }*/

    /*override fun getWeatherInformation(lat: Double, long:Double): Flow<Response<WeatherInfo>> = flow {
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
    }.flowOn(Dispatchers.IO)*/

    override suspend fun getLastWeatherInformation(): Flow<Response<WeatherInfo>> = flow {
        val weatherDao = WeatherApp.db.WeatherDao()
        val info = weatherDao.getAll()
        if(info.isEmpty().not()) {
            var weatherInfo = Gson().fromJson(info[0].weatherInfo, WeatherInfo::class.java)

            emit(Response.Success(weatherInfo))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun setWeatherInformation(info: String) {


        val weatherDao = WeatherApp.db.WeatherDao()
        weatherDao.delete()
        weatherDao.insert(LastWeatherInfo(1, info))
    }


}