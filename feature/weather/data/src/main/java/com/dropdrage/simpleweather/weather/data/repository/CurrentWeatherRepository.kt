package com.dropdrage.simpleweather.weather.data.repository

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.data.domain.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(locations: List<Location>): Flow<List<CurrentWeather?>>
}
