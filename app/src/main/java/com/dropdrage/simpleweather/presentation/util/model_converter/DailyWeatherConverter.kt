package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.core.domain.Range
import com.dropdrage.simpleweather.domain.weather.DayWeather
import com.dropdrage.simpleweather.presentation.model.ViewDayWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.DateFormatter
import com.dropdrage.simpleweather.presentation.util.format.DayNameFormat
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import java.time.LocalDate
import javax.inject.Inject

class DailyWeatherConverter @Inject constructor(
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