package com.dropdrage.simpleweather.data.dto

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class HourlyWeatherDto(
    @Json(name = "time") val time: List<LocalDateTime>,
    @Json(name = "weathercode") val weatherCodes: List<Int>,
    @Json(name = "pressure_msl") val pressures: List<Float>,
    @Json(name = "windspeed_10m") val windSpeeds: List<Float>,
    @Json(name = "temperature_2m") val temperatures: List<Float>,
    @Json(name = "relativehumidity_2m") val humidities: List<Int>,
)
