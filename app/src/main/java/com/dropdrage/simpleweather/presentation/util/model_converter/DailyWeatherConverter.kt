package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.domain.util.Range
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.DateFormatter
import com.dropdrage.simpleweather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import javax.inject.Inject

class DailyWeatherConverter @Inject constructor(
    private val timeFormatter: TimeFormatter,
    private val dateFormatter: DateFormatter,
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(dayWeather: DayWeather): ViewDayWeather = ViewDayWeather(
        date = dayWeather.date,
        dateFormatted = dateFormatter.formatDayMonth(dayWeather.date),
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
        sunriseTime = timeFormatter.formatTime(dayWeather.sunrise),
        sunsetTime = timeFormatter.formatTime(dayWeather.sunset),
    )
}