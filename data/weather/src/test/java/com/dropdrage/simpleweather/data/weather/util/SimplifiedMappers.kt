package com.dropdrage.simpleweather.data.weather.util

import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import com.dropdrage.simpleweather.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.weather.domain.weather.Weather

internal fun List<DayToHourWeather>.simplyToDomainWeather() = Weather(map(DayToHourWeather::simplyToDayWeather))

internal fun DayToHourWeather.simplyToDayWeather() = DayWeather(
    date = day.date,
    weatherType = day.weatherType,
    temperatureRange = day.temperatureRange.toRange(),
    apparentTemperatureRange = day.apparentTemperatureRange.toRange(),
    precipitationSum = day.precipitationSum,
    maxWindSpeed = day.maxWindSpeed,
    sunrise = day.sunrise,
    sunset = day.sunset,
    weatherPerHour = hours.map {
        HourWeather(
            dateTime = it.dateTime,
            weatherType = it.weatherType,
            temperature = it.temperature,
            pressure = it.pressure,
            windSpeed = it.windSpeed,
            humidity = it.humidity,
            visibility = it.visibility,
        )
    },
)
