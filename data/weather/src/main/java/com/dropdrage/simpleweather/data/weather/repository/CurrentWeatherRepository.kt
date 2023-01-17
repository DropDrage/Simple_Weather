package com.dropdrage.simpleweather.data.weather.repository

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.domain.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(locations: List<Location>): Flow<List<CurrentWeather?>>
}
