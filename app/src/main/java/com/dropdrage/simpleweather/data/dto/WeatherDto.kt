package com.dropdrage.simpleweather.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDto(
    @Json(name = "hourly") val hourly: HourlyWeatherDto,
)