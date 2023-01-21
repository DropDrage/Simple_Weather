package com.dropdrage.simpleweather.weather.data.repository

import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.data.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherFromNow(location: Location): Resource<Weather>

    suspend fun getUpdatedWeatherFromNow(location: Location): Flow<Resource<Weather>>

    suspend fun updateWeather(location: Location)

}