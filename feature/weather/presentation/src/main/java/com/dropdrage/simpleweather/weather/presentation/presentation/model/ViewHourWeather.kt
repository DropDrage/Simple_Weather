package com.dropdrage.simpleweather.weather.presentation.model

import com.dropdrage.adapters.differ.SameEquatable
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import java.time.LocalDateTime

internal data class ViewHourWeather(
    private val dateTime: LocalDateTime,
    val timeFormatted: String,
    val isNow: Boolean,
    val weatherType: ViewWeatherType,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
) : SameEquatable<ViewHourWeather> {
    override fun isSame(other: ViewHourWeather) = dateTime == other.dateTime
}
