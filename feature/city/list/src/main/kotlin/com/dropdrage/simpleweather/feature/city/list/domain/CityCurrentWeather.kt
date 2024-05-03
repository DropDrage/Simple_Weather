package com.dropdrage.simpleweather.feature.city.list.domain

import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.list.domain.weather.CurrentWeather

internal data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)
