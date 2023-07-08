package com.akshaymayekar.weatherapp.di

import com.akshaymayekar.weatherapp.data.WeatherLocalRepositoryImpl
import com.akshaymayekar.weatherapp.data.WeatherRepositoryImpl
import com.akshaymayekar.weatherapp.data.service.WeatherInfoService
import com.akshaymayekar.weatherapp.domain.WeatherLocalRepository
import com.akshaymayekar.weatherapp.domain.WeatherRepository
import com.akshaymayekar.weatherapp.util.Const.KEY_API
import com.akshaymayekar.weatherapp.util.Const.WEB_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    @Named("WEB_API")
    fun provideWebAPI(): String = WEB_API

    @Provides
    @Named("KEY_API")
    fun provideKeyAPI(): String = KEY_API


    @Provides
    fun provideRetrofit(
        @Named("WEB_API") webAPI: String,
    ): Retrofit {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        return Retrofit.Builder()
            .baseUrl(webAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideNowPlayingService(
        retrofit: Retrofit
    ): WeatherInfoService = retrofit.create(WeatherInfoService::class.java)


    @Provides
    fun provideWeatherRepository(
        weatherInfoService: WeatherInfoService,
        @Named("KEY_API") apiKey: String
    ): WeatherRepository = WeatherRepositoryImpl(
        weatherInfoService = weatherInfoService,
        apiKey = apiKey
    )

    @Provides
    fun provideWeatherLocalRepository(): WeatherLocalRepository = WeatherLocalRepositoryImpl()
}