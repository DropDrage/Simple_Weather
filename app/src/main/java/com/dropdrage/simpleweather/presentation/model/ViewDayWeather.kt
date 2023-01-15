package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.core.domain.Range
import java.time.LocalDate

data class ViewDayWeather(
    private val date: LocalDate,
    val dayTitle: String,
    val dateFormatted: String,
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
) : com.dropdrage.adapters.differ.SameEquatable<ViewDayWeather> {
    override fun isSame(other: ViewDayWeather) = date == other.date
}
