package com.dropdrage.simpleweather.data.weather.di

import com.dropdrage.simpleweather.data.weather.BuildConfig
import com.dropdrage.simpleweather.data.weather.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {
    @[Provides Singleton]
    fun provideWeatherApi(okHttpClient: OkHttpClient, jsonConverterFactory: Converter.Factory): WeatherApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ApiSupportedParamFactory())
            .addConverterFactory(jsonConverterFactory)
            .baseUrl(BuildConfig.WEATHER_URL)
            .build()
            .create(WeatherApi::class.java)
}
