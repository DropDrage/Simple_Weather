package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.util.Range
import java.time.LocalDate

data class ViewDayWeather(
    val date: LocalDate,
    val dateFormatted: String,
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
    val apparentTemperatureRange: Range<String>,
    val precipitationSum: String,
    val maxWindSpeed: String,
    val sunriseTime: String,
    val sunsetTime: String,
)
