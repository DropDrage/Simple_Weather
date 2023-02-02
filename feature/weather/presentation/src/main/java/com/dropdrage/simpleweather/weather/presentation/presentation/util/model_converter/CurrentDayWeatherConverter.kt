package com.dropdrage.simpleweather.weather.presentation.util.model_converter

import com.dropdrage.common.domain.Range
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.weather.presentation.model.ViewCurrentDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.format.TimeFormatter
import javax.inject.Inject

internal class CurrentDayWeatherConverter @Inject constructor(
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
        sunriseTime = dayWeather.sunrise,
        sunsetTime = dayWeather.sunset,
        sunriseFormatted = timeFormatter.formatTime(dayWeather.sunrise),
        sunsetFormatted = timeFormatter.formatTime(dayWeather.sunset),
    )
}
