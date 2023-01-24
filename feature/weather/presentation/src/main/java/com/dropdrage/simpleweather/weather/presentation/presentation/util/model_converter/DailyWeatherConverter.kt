package com.dropdrage.simpleweather.weather.presentation.util.model_converter

import com.dropdrage.simpleweather.common.domain.Range
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.weather.domain.DayWeather
import com.dropdrage.simpleweather.weather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.format.DateFormatter
import com.dropdrage.simpleweather.weather.presentation.util.format.DayNameFormat
import java.time.LocalDate
import javax.inject.Inject

internal class DailyWeatherConverter @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dayFormatter: DayNameFormat,
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(dayWeather: DayWeather, now: LocalDate): ViewDayWeather = ViewDayWeather(
        date = dayWeather.date,
        dayTitle = dayFormatter.formatFromStartDay(dayWeather.date, now),
        dateFormatted = dateFormatter.formatDayMonth(dayWeather.date),
        weatherType = ViewWeatherType.fromWeatherType(dayWeather.weatherType),
        temperatureRange = Range(
            unitsFormatter.formatTemperature(dayWeather.temperatureRange.start),
            unitsFormatter.formatTemperature(dayWeather.temperatureRange.end)
        ),
    )
}