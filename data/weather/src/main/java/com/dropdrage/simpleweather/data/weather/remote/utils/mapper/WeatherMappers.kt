package com.dropdrage.simpleweather.data.weather.remote

import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter.convertTemperatureIfApiDontSupport
import com.dropdrage.simpleweather.feature.city.list.domain.weather.CurrentWeather

internal fun CurrentWeatherResponseDto.toDomainCurrentWeather(): CurrentWeather = CurrentWeather(
    convertTemperatureIfApiDontSupport(currentWeather.temperature),
    WeatherTypeConverter.toWeatherType(currentWeather.weatherCode)
)
