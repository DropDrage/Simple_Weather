package com.dropdrage.simpleweather.weather.presentation.model

import com.dropdrage.common.domain.Range
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import java.time.LocalDateTime

internal data class ViewCurrentDayWeather(
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
    val apparentTemperatureRange: Range<String>,
    val precipitationSum: String,
    val maxWindSpeed: String,
    val sunriseTime: LocalDateTime,
    val sunsetTime: LocalDateTime,
    val sunriseFormatted: String,
    val sunsetFormatted: String,
)
