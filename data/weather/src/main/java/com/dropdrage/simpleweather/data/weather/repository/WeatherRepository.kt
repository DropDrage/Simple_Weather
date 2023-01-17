package com.dropdrage.simpleweather.data.weather.repository

import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherFromNow(location: Location): Resource<Weather>

    suspend fun getUpdatedWeatherFromNow(location: Location): Flow<Resource<Weather>>

    suspend fun updateWeather(location: Location)

}