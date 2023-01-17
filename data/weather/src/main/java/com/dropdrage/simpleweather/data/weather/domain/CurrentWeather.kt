package com.dropdrage.simpleweather.data.weather.domain

import com.dropdrage.simpleweather.core.domain.weather.WeatherType

data class CurrentWeather(
    val temperature: Float,
    val weatherType: WeatherType,
)
