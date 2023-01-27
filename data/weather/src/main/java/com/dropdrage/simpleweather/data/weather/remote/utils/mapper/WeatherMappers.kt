package com.dropdrage.simpleweather.data.weather.remote

import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter.convertTemperatureIfApiDontSupport

internal fun CurrentWeatherResponseDto.toDomainCurrentWeather(): CurrentWeather = CurrentWeather(
    convertTemperatureIfApiDontSupport(currentWeather.temperature),
    WeatherTypeConverter.toWeatherType(currentWeather.weatherCode)
)
