package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.data.util.LogTags
import com.dropdrage.simpleweather.data.util.toDomainWeather
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
) : SimpleRepository<Weather>(LogTags.WEATHER), WeatherRepository {
    override suspend fun getWeather(location: Location): Resource<Weather> = simplyResourceWrap {
        api.getWeather(
            location.latitude, location.longitude,
            WeatherUnitsPreferences.temperatureUnit,
            WeatherUnitsPreferences.windSpeedUnit,
            WeatherUnitsPreferences.precipitationUnit,
        ).toDomainWeather()
    }
}
