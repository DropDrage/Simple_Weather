package com.dropdrage.simpleweather.presentation.model

data class ViewCurrentHourWeather(
    val weatherType: ViewWeatherType,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
)
