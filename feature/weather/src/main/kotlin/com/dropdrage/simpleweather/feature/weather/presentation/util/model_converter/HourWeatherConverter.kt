package com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.TimeFormatter
import java.time.LocalDateTime
import javax.inject.Inject

internal class HourWeatherConverter @Inject constructor(
    private val timeFormatter: TimeFormatter,
    private val unitsFormatter: WeatherUnitsFormatter,
) {

    fun convertToView(hourWeather: HourWeather, currentDateTime: LocalDateTime): ViewHourWeather {
        val isNow = isNow(hourWeather.dateTime, currentDateTime)
        return ViewHourWeather(
            dateTime = hourWeather.dateTime,
            timeFormatted = timeFormatter.formatAsHourOrNow(hourWeather.dateTime, isNow),
            isNow = isNow,
            weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
            temperature = unitsFormatter.formatTemperature(hourWeather.temperature),
            pressure = unitsFormatter.formatPressure(hourWeather.pressure),
            windSpeed = unitsFormatter.formatWindSpeed(hourWeather.windSpeed),
            humidity = unitsFormatter.formatHumidity(hourWeather.humidity),
            visibility = unitsFormatter.formatVisibility(hourWeather.visibility),
        )
    }

    fun convertToView(hourWeathers: List<HourWeather>, currentDateTime: LocalDateTime): List<ViewHourWeather> =
        timeFormatter.bulkFormat {
            unitsFormatter.bulkFormat {
                hourWeathers.map { hourWeather ->
                    val isNow = isNow(hourWeather.dateTime, currentDateTime)
                    ViewHourWeather(
                        dateTime = hourWeather.dateTime,
                        timeFormatted = formatAsHourOrNow(hourWeather.dateTime, isNow),
                        isNow = isNow,
                        weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
                        temperature = formatTemperature(hourWeather.temperature),
                        pressure = formatPressure(hourWeather.pressure),
                        windSpeed = formatWindSpeed(hourWeather.windSpeed),
                        humidity = formatHumidity(hourWeather.humidity),
                        visibility = formatVisibility(hourWeather.visibility),
                    )
                }
            }
        }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun isNow(dateTime: LocalDateTime, currentDateTime: LocalDateTime) =
        dateTime.hour == currentDateTime.hour && dateTime.dayOfMonth == currentDateTime.dayOfMonth

}
