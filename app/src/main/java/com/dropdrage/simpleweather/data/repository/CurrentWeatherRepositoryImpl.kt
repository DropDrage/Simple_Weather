package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.data.util.LogTags
import com.dropdrage.simpleweather.data.util.mapper.toDomainCurrentWeather
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeather
import com.dropdrage.simpleweather.domain.weather.current.CurrentWeatherRepository
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
) : SimpleRepository<CurrentWeather>(LogTags.WEATHER), CurrentWeatherRepository {
    override suspend fun getCurrentWeather(location: Location): Resource<CurrentWeather> = simplyResourceWrap {
        api.getCurrentWeather(location.latitude, location.longitude, WeatherUnitsPreferences.temperatureUnit)
            .toDomainCurrentWeather()
    }
}