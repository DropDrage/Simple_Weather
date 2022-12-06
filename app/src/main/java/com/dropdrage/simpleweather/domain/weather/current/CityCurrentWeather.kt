package com.dropdrage.simpleweather.domain.weather.current

import com.dropdrage.simpleweather.domain.city.search.City

data class CityCurrentWeather(
    val city: City,
    val weather: CurrentWeather?,
)