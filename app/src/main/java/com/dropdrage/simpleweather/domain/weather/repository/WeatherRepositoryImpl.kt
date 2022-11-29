package com.dropdrage.simpleweather.domain.weather.repository

import com.dropdrage.simpleweather.data.remote.WeatherApi
import com.dropdrage.simpleweather.data.util.toDomainWeather
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double): Resource<Weather> {
        return try {
            Resource.Success(api.getWeather(latitude, longitude).toDomainWeather())
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}