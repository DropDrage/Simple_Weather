package com.dropdrage.simpleweather.domain.weather.current

import com.dropdrage.simpleweather.domain.location.Location
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(locations: List<Location>): Flow<List<CurrentWeather?>>
}
