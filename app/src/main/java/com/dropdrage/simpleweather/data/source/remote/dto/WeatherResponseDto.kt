package com.dropdrage.simpleweather.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class WeatherResponseDto(
    @Json(name = "hourly") val hourly: HourlyWeatherDto,
)

data class HourlyWeatherDto(
    @Json(name = "time") val time: List<LocalDateTime>,
    @Json(name = "weathercode") val weatherCodes: List<Int>,
    @Json(name = "pressure_msl") val pressures: List<Float>,
    @Json(name = "windspeed_10m") val windSpeeds: List<Float>,
    @Json(name = "temperature_2m") val temperatures: List<Float>,
    @Json(name = "relativehumidity_2m") val humidities: List<Int>,
)
