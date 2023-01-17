package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType

data class ViewCurrentHourWeather(
    val weatherType: ViewWeatherType,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
)
