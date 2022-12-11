package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.util.Range
import com.dropdrage.simpleweather.presentation.util.adapter.differ.SameEquatable
import java.time.LocalDate

data class ViewDayWeather(
    private val date: LocalDate,
    val dayTitle: String,
    val dateFormatted: String,
    val weatherType: ViewWeatherType,
    val temperatureRange: Range<String>,
) : SameEquatable<ViewDayWeather> {
    override fun isSame(other: ViewDayWeather) = date == other.date
}
