package com.dropdrage.simpleweather.weather.presentation.model

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType

internal data class ViewCurrentHourWeather(
    val weatherType: ViewWeatherType,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
)
