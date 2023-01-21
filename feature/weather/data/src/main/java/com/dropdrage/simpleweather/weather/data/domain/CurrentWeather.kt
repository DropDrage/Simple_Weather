package com.dropdrage.simpleweather.weather.data.domain

import com.dropdrage.simpleweather.core.domain.weather.WeatherType

data class CurrentWeather(
    val temperature: Float,
    val weatherType: WeatherType,
)
