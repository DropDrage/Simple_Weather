package com.dropdrage.simpleweather.domain.weather

import com.dropdrage.simpleweather.core.domain.Range
import java.time.LocalDate
import java.time.LocalDateTime

data class DayWeather(
    val date: LocalDate,
    val weatherType: WeatherType,
    val temperatureRange: Range<Float>,
    val apparentTemperatureRange: Range<Float>,
    val precipitationSum: Float,
    val maxWindSpeed: Float,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,

    val weatherPerHour: List<HourWeather>,
)