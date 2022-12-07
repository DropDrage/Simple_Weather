package com.dropdrage.simpleweather.domain.weather

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