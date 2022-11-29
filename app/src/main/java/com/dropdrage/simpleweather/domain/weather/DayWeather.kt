package com.dropdrage.simpleweather.domain.weather

data class DayWeather(
    val weatherPerHour: List<HourWeather>,
)