package com.dropdrage.simpleweather.domain.weather

import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource

interface WeatherRepository {
    suspend fun getWeather(location: Location): Resource<Weather>
}