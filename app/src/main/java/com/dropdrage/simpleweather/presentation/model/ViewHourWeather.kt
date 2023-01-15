package com.dropdrage.simpleweather.presentation.model

import java.time.LocalDateTime

data class ViewHourWeather(
    private val dateTime: LocalDateTime,
    val timeFormatted: String,
    val isNow: Boolean,
    val weatherType: ViewWeatherType,
    val temperature: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val visibility: String,
) : com.dropdrage.adapters.differ.SameEquatable<ViewHourWeather> {
    override fun isSame(other: ViewHourWeather) = dateTime == other.dateTime
}
