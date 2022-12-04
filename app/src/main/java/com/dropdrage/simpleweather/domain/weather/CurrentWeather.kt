package com.dropdrage.simpleweather.domain.weather

data class CurrentWeather(
    val temperatureCelsius: Float,
    val weatherType: WeatherType,
)
