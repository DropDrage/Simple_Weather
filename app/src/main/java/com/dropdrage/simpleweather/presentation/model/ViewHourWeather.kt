package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.presentation.util.adapter.differ.SameEquatable
import java.time.LocalDateTime

data class ViewHourWeather(
    private val dateTime: LocalDateTime,
    val weatherType: ViewWeatherType,
    val timeFormatted: String,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
) : SameEquatable<ViewHourWeather> {

    val hour: Int = dateTime.hour
    val dayOfMonth: Int = dateTime.dayOfMonth

    override fun isSame(other: ViewHourWeather) = dateTime == other.dateTime
}
