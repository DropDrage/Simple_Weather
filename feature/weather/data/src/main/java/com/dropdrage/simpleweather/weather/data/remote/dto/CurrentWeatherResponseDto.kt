package com.dropdrage.simpleweather.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CurrentWeatherResponseDto(
    @Json(name = "current_weather") val currentWeather: CurrentWeatherDto,
)

internal data class CurrentWeatherDto(
    @Json(name = "temperature") val temperature: Float,
    @Json(name = "weathercode") val weatherCode: Int,
)