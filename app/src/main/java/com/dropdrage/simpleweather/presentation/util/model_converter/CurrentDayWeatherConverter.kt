package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.core.domain.Range
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import javax.inject.Inject

class CurrentDayWeatherConverter @Inject constructor(
    private val timeFormatter: TimeFormatter,
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(dayWeather: DayWeather): ViewCurrentDayWeather = ViewCurrentDayWeather(
        weatherType = ViewWeatherType.fromWeatherType(dayWeather.weatherType),
        temperatureRange = Range(
            unitsFormatter.formatTemperature(dayWeather.temperatureRange.start),
            unitsFormatter.formatTemperature(dayWeather.temperatureRange.end)
        ),
        apparentTemperatureRange = Range(
            unitsFormatter.formatTemperature(dayWeather.apparentTemperatureRange.start),
            unitsFormatter.formatTemperature(dayWeather.apparentTemperatureRange.end)
        ),
        precipitationSum = unitsFormatter.formatPrecipitation(dayWeather.precipitationSum),
        maxWindSpeed = unitsFormatter.formatWindSpeed(dayWeather.maxWindSpeed),
        sunrise = dayWeather.sunrise,
        sunset = dayWeather.sunset,
        sunriseTime = timeFormatter.formatTime(dayWeather.sunrise),
        sunsetTime = timeFormatter.formatTime(dayWeather.sunset),
    )
}