package com.akshaymayekar.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshaymayekar.weatherapp.domain.WeatherLocalRepository
import com.akshaymayekar.weatherapp.domain.WeatherRepository
import com.akshaymayekar.weatherapp.domain.model.WeatherInfo
import com.akshaymayekar.weatherapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private var weatherRepository: WeatherRepository,
    private var weatherLocalRepository: WeatherLocalRepository
) : ViewModel() {


    private val _weatherState = MutableStateFlow<Response<WeatherInfo>>(Response.Success(null))
    val weatherState: StateFlow<Response<WeatherInfo>> = _weatherState


    fun getWeatherData(lat: Double, lon: Double) {

        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherInformation(lat, lon).collect {
                _weatherState.value = it

            }
        }

    }

    fun saveToLocalDB(toString: String) {
        viewModelScope.launch(Dispatchers.Default) {
            weatherLocalRepository.setWeatherInformation(toString)
        }
    }

    fun getWeatherDataFromLocal() {
        viewModelScope.launch(Dispatchers.Default) {
            weatherLocalRepository.getLastWeatherInformation().collect {

                _weatherState.value = it

            }
        }
    }
}