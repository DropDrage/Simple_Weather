package com.dropdrage.simpleweather.data.weather.util

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.TemperatureRange
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.data.weather.remote.DailyWeatherDto
import com.dropdrage.simpleweather.data.weather.remote.HourlyWeatherDto
import com.dropdrage.test.util.createList
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

internal fun createDayToHourWeather(hourCount: Int): DayToHourWeather {
    val dayId = Random.nextLong()
    return DayToHourWeather(
        DayWeatherModel(
            id = dayId,
            date = LocalDate.now(),
            locationId = Random.nextLong(),
            weatherType = WeatherType.values().random(),
            temperatureRange = TemperatureRange(Random.nextFloat(), Random.nextFloat()),
            apparentTemperatureRange = TemperatureRange(Random.nextFloat(), Random.nextFloat()),
            precipitationSum = Random.nextFloat(),
            maxWindSpeed = Random.nextFloat(),
            sunrise = LocalDateTime.now(),
            sunset = LocalDateTime.now(),
        ),
        createList(hourCount) { createHourWeatherModel(dayId) },
    )
}

private fun createHourWeatherModel(dayId: Long) = HourWeatherModel(
    id = Random.nextLong(),
    dateTime = LocalDateTime.now(),
    dayId = dayId,
    weatherType = WeatherType.values().random(),
    temperature = Random.nextFloat(),
    pressure = Random.nextInt(),
    windSpeed = Random.nextFloat(),
    humidity = Random.nextInt(),
    visibility = Random.nextFloat(),
)


internal fun createDailyWeatherDto(daysCount: Int) = DailyWeatherDto(
    dates = createList(daysCount) { LocalDate.now() },
    weatherCodes = createList(daysCount) { Random.nextInt(0, 100) },
    minTemperatures = createList(daysCount) { Random.nextFloat() },
    maxTemperatures = createList(daysCount) { Random.nextFloat() },
    apparentMinTemperatures = createList(daysCount) { Random.nextFloat() },
    apparentMaxTemperatures = createList(daysCount) { Random.nextFloat() },
    precipitationSums = createList(daysCount) { Random.nextFloat() },
    maxWindSpeeds = createList(daysCount) { Random.nextFloat() },
    sunrises = createList(daysCount) { LocalDateTime.now() },
    sunsets = createList(daysCount) { LocalDateTime.now() },
)

internal fun createHourlyWeatherDto(hoursCount: Int) = HourlyWeatherDto(
    time = createList(hoursCount) { LocalDateTime.now() },
    weatherCodes = createList(hoursCount) { Random.nextInt(0, 100) },
    pressures = createList(hoursCount) { Random.nextFloat() },
    windSpeeds = createList(hoursCount) { Random.nextFloat() },
    temperatures = createList(hoursCount) { Random.nextFloat() },
    humidities = createList(hoursCount) { Random.nextInt() },
    visibilities = createList(hoursCount) { Random.nextInt() },
)
