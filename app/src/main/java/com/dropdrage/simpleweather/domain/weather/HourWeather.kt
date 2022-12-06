package com.dropdrage.simpleweather.domain.weather

import java.time.LocalDateTime

data class HourWeather(
    val dateTime: LocalDateTime,
    val temperature: Float,
    val pressure: Float,
    val windSpeed: Float,
    val humidity: Int,
    val weatherType: WeatherType,
)