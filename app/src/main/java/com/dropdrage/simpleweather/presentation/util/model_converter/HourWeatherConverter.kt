package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.domain.weather.HourWeather
import com.dropdrage.simpleweather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsConverter
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import javax.inject.Inject

class HourWeatherConverter @Inject constructor(
    private val unitsConverter: WeatherUnitsConverter,
    private val timeFormatter: TimeFormatter,
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(hourWeather: HourWeather): ViewHourWeather = ViewHourWeather(
        dateTime = hourWeather.dateTime,
        timeFormatted = timeFormatter.formatAsHour(hourWeather.dateTime),
        temperature = unitsFormatter.formatTemperature(unitsConverter.convertTemperature(hourWeather.temperature)),
        pressure = unitsFormatter.formatPressure(unitsConverter.convertPressure(hourWeather.pressure)),
        windSpeed = unitsFormatter.formatWindSpeed(unitsConverter.convertWindSpeed(hourWeather.windSpeed)),
        humidity = unitsFormatter.formatHumidity(hourWeather.humidity),
        weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
    )
}