package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.city.search.City

data class ViewCityCurrentWeather(
    val city: City,
    val currentWeather: ViewCurrentWeather,
)

data class ViewCurrentWeather(
    val temperature: String,
    val weatherType: ViewWeatherType,
)
