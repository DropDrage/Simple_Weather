package com.dropdrage.simpleweather.domain.weather

import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeatherFromNow(location: Location): Flow<Resource<Weather>>

    suspend fun updateWeather(location: Location)

}