package com.dropdrage.simpleweather.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponseDto(
    @Json(name = "current_weather") val currentWeather: CurrentWeatherDto,
)

data class CurrentWeatherDto(
    @Json(name = "temperature") val temperature: Float,
    @Json(name = "weathercode") val weatherCode: Int,
)