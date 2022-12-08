package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.util.Range
import java.time.LocalDateTime

data class ViewCurrentDayWeather(
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
    val apparentTemperatureRange: Range<String>,
    val precipitationSum: String,
    val maxWindSpeed: String,
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime,
    val sunriseTime: String,
    val sunsetTime: String,
)
