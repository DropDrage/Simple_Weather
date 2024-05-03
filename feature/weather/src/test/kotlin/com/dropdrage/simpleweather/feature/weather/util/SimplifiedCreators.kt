package com.dropdrage.simpleweather.feature.weather.util

import com.dropdrage.common.domain.Range
import com.dropdrage.common.test.util.createListIndexed
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.feature.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.feature.weather.domain.weather.HourWeather
import java.time.LocalDateTime
import kotlin.random.Random

internal const val HOURS_IN_DAY = 24


internal fun createDayWeather(day: Long): DayWeather {
    val date = LocalDateTime.now().plusDays(day)
    return DayWeather(
        date = date.toLocalDate(),
        weatherType = WeatherType.values().random(),
        temperatureRange = Range(Random.nextFloat(), Random.nextFloat()),
        apparentTemperatureRange = Range(Random.nextFloat(), Random.nextFloat()),
        precipitationSum = Random.nextFloat(),
        maxWindSpeed = Random.nextFloat(),
        sunrise = date,
        sunset = date,
        weatherPerHour = createListIndexed(HOURS_IN_DAY) { createHourWeather(date, it) },
    )
}


internal fun createHourWeather(date: LocalDateTime) = HourWeather(
    dateTime = date,
    weatherType = WeatherType.values().random(),
    temperature = Random.nextFloat(),
    pressure = Random.nextInt(),
    windSpeed = Random.nextFloat(),
    humidity = Random.nextInt(),
    visibility = Random.nextFloat(),
)

internal fun createHourWeather(date: LocalDateTime, currentHour: Int) = createHourWeather(date.withHour(currentHour))


internal fun createDayWeatherNoHours(day: Long): DayWeather {
    val date = LocalDateTime.now().plusDays(day)
    return DayWeather(
        date = date.toLocalDate(),
        weatherType = WeatherType.values().random(),
        temperatureRange = Range(Random.nextFloat(), Random.nextFloat()),
        apparentTemperatureRange = Range(Random.nextFloat(), Random.nextFloat()),
        precipitationSum = Random.nextFloat(),
        maxWindSpeed = Random.nextFloat(),
        sunrise = date,
        sunset = date,
        weatherPerHour = emptyList(),
    )
}
