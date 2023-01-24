package com.dropdrage.simpleweather.city_list.domain.weather

import com.dropdrage.simpleweather.core.domain.weather.WeatherType

data class CurrentWeather(
    val temperature: Float,
    val weatherType: WeatherType,
)
