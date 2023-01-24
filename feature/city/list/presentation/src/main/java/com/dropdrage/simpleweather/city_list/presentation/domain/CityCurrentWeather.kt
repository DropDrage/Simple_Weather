package com.dropdrage.simpleweather.city_list.presentation.domain

import com.dropdrage.simpleweather.city_list.domain.city.City
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather

internal data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)
