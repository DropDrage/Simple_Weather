package com.dropdrage.simpleweather.city.list.domain

import com.dropdrage.simpleweather.data.city.domain.City
import com.dropdrage.simpleweather.data.weather.domain.CurrentWeather

internal data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)
