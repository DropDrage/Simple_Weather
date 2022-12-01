package com.dropdrage.simpleweather.domain.weather

import com.dropdrage.simpleweather.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Resource<Weather>
}