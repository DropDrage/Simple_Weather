package com.dropdrage.simpleweather.domain.weather

import com.dropdrage.simpleweather.core.domain.Resource
import com.dropdrage.simpleweather.domain.location.Location
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherFromNow(location: Location): Resource<Weather>

    suspend fun getUpdatedWeatherFromNow(location: Location): Flow<Resource<Weather>>

    suspend fun updateWeather(location: Location)

}