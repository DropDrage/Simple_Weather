package com.dropdrage.simpleweather.feature.weather.domain.weather

import com.dropdrage.common.domain.Range
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
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