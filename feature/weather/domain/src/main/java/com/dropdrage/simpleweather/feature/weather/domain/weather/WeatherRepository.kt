package com.dropdrage.simpleweather.feature.weather.domain.weather

import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.core.domain.Location
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherFromNow(location: Location): Resource<Weather>

    suspend fun getUpdatedWeatherFromNow(location: Location): Flow<Resource<Weather>>

    suspend fun updateWeather(location: Location)

}