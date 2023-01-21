package com.dropdrage.simpleweather.weather.data.local.cache.dto

import com.dropdrage.simpleweather.core.domain.weather.WeatherType

internal data class CurrentWeatherDto(val temperature: Float, val weatherType: WeatherType)
