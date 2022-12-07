package com.dropdrage.simpleweather.presentation.model

import java.time.LocalDateTime

data class ViewHourWeather(
    val dateTime: LocalDateTime,
    val weatherType: ViewWeatherType,
    val timeFormatted: String,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
)
