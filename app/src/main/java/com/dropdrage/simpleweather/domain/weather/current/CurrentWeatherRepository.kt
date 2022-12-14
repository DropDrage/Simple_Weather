package com.dropdrage.simpleweather.domain.weather.current

import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(location: Location): Resource<CurrentWeather>
}