package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.data.util.toDomainWeather
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double): Resource<Weather> = try {
        Resource.Success(api.getWeather(latitude, longitude).toDomainWeather())
    } catch (e: Exception) {
        Resource.Error(e.message, e)
    }
}