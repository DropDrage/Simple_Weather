package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.city.City

data class ViewCityCurrentWeather(
    val city: City,
    val currentWeather: ViewCurrentWeather,
) : com.dropdrage.adapters.differ.SameEquatable<ViewCityCurrentWeather> {
    override fun isSame(other: ViewCityCurrentWeather): Boolean = other.city == city
}

data class ViewCurrentWeather(
    val temperature: String,
    val weatherType: ViewWeatherType,
)
