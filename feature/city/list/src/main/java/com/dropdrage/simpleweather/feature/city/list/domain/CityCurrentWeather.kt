package com.dropdrage.simpleweather.feature.city.list.domain

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather

internal data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)
