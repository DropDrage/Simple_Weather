package com.dropdrage.simpleweather.feature.city.list.presentation.model

import com.dropdrage.adapters.differ.SameEquatable
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.feature.city.domain.City

internal data class ViewCityCurrentWeather(
    val city: City,
    val currentWeather: ViewCurrentWeather,
) : SameEquatable<ViewCityCurrentWeather> {
    override fun isSame(other: ViewCityCurrentWeather): Boolean = other.city == city
}

internal data class ViewCurrentWeather(
    val temperature: String,
    val weatherType: ViewWeatherType,
)
