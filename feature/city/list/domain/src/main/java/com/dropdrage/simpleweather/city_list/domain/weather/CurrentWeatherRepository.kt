package com.dropdrage.simpleweather.city_list.domain.weather

import com.dropdrage.simpleweather.core.domain.Location
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(locations: List<Location>): Flow<List<CurrentWeather?>>
}
