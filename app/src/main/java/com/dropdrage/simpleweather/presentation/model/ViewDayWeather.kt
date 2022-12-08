package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.util.Range
import java.time.LocalDate

data class ViewDayWeather(
    val day: String,
    val date: LocalDate,
    val dateFormatted: String,
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
)
