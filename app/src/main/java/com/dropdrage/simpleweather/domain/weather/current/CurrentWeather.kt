package com.dropdrage.simpleweather.domain.weather.current

import com.dropdrage.simpleweather.domain.weather.WeatherType

data class CurrentWeather(
    val temperature: Float,
    val weatherType: WeatherType,
)
