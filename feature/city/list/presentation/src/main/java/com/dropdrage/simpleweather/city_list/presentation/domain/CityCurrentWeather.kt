package com.dropdrage.simpleweather.city_list.presentation.domain

import com.dropdrage.simpleweather.city_list.data.domain.City
import com.dropdrage.simpleweather.weather.data.domain.CurrentWeather

internal data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)
