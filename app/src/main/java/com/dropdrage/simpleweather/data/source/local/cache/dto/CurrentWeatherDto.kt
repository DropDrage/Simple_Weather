package com.dropdrage.simpleweather.data.source.local.cache.dto

import com.dropdrage.simpleweather.domain.weather.WeatherType

data class CurrentWeatherDto(val temperature: Float, val weatherType: WeatherType)
