package com.dropdrage.simpleweather.data.weather.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
internal data class WeatherResponseDto(
    @Json(name = "daily") val daily: DailyWeatherDto,
    @Json(name = "hourly") val hourly: HourlyWeatherDto,
)

internal data class DailyWeatherDto(
    @Json(name = "time") val dates: List<LocalDate>,
    @Json(name = "weathercode") val weatherCodes: List<Int>,
    @Json(name = "temperature_2m_min") val minTemperatures: List<Float>,
    @Json(name = "temperature_2m_max") val maxTemperatures: List<Float>,
    @Json(name = "apparent_temperature_min") val apparentMinTemperatures: List<Float>,
    @Json(name = "apparent_temperature_max") val apparentMaxTemperatures: List<Float>,
    @Json(name = "precipitation_sum") val precipitationSums: List<Float>,
    @Json(name = "windspeed_10m_max") val maxWindSpeeds: List<Float>,
    @Json(name = "sunrise") val sunrises: List<LocalDateTime>,
    @Json(name = "sunset") val sunsets: List<LocalDateTime>,
)

internal data class HourlyWeatherDto(
    @Json(name = "time") val time: List<LocalDateTime>,
    @Json(name = "weathercode") val weatherCodes: List<Int>,
    @Json(name = "pressure_msl") val pressures: List<Float>,
    @Json(name = "windspeed_10m") val windSpeeds: List<Float>,
    @Json(name = "temperature_2m") val temperatures: List<Float>,
    @Json(name = "relativehumidity_2m") val humidities: List<Int>,
    @Json(name = "visibility") val visibilities: List<Int>,
)
