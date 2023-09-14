package com.dropdrage.simpleweather.feature.weather.util

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.feature.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.feature.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCurrentHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather

internal fun toViewCurrentDayWeather(currentDayWeather: DayWeather) = ViewCurrentDayWeather(
    weatherType = ViewWeatherType.fromWeatherType(currentDayWeather.weatherType),
    temperatureRange = currentDayWeather.temperatureRange.map(Float::toString),
    apparentTemperatureRange = currentDayWeather.apparentTemperatureRange.map(Float::toString),
    precipitationSum = currentDayWeather.precipitationSum.toString(),
    maxWindSpeed = currentDayWeather.maxWindSpeed.toString(),
    sunrise = currentDayWeather.sunrise,
    sunset = currentDayWeather.sunset,
    sunriseTime = currentDayWeather.sunrise.toString(),
    sunsetTime = currentDayWeather.sunset.toString(),
)


internal fun toViewCurrentHourWeather(currentHourWeather: HourWeather) = ViewCurrentHourWeather(
    weatherType = ViewWeatherType.fromWeatherType(currentHourWeather.weatherType),
    temperature = currentHourWeather.temperature.toString(),
    pressure = currentHourWeather.pressure.toString(),
    windSpeed = currentHourWeather.windSpeed.toString(),
    humidity = currentHourWeather.humidity.toString(),
    visibility = currentHourWeather.visibility.toString(),
)


internal fun toViewDayWeather(dayWeather: DayWeather) = ViewDayWeather(
    date = dayWeather.date,
    dayTitle = dayWeather.date.dayOfWeek.toString(),
    dateFormatted = dayWeather.date.toString(),
    weatherType = ViewWeatherType.fromWeatherType(dayWeather.weatherType),
    temperatureRange = dayWeather.temperatureRange.map(Float::toString)
)


internal fun toViewHourWeather(hourWeather: HourWeather, isNow: Boolean = false) = ViewHourWeather(
    dateTime = hourWeather.dateTime,
    timeFormatted = hourWeather.dateTime.toLocalTime().toString(),
    isNow = isNow,
    weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
    temperature = hourWeather.temperature.toString(),
    pressure = hourWeather.pressure.toString(),
    windSpeed = hourWeather.windSpeed.toString(),
    humidity = hourWeather.humidity.toString(),
    visibility = hourWeather.visibility.toString(),
)
