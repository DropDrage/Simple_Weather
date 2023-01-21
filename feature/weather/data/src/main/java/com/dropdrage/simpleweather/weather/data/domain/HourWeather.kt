package com.dropdrage.simpleweather.weather.data.domain

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import java.time.LocalDateTime

data class HourWeather(
    val dateTime: LocalDateTime,
    val weatherType: WeatherType,
    val temperature: Float,
    val pressure: Int,
    val windSpeed: Float,
    val humidity: Int,
    val visibility: Float,
)
