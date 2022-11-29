package com.dropdrage.simpleweather.domain.weather.repository

import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Resource<Weather>
}