package com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCurrentHourWeather
import com.dropdrage.simpleweather.weather.domain.weather.HourWeather
import javax.inject.Inject

internal class CurrentHourWeatherConverter @Inject constructor(private val unitsFormatter: WeatherUnitsFormatter) {
    fun convertToView(hourWeather: HourWeather) = ViewCurrentHourWeather(
        weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
        temperature = unitsFormatter.formatTemperature(hourWeather.temperature),
        pressure = unitsFormatter.formatPressure(hourWeather.pressure),
        windSpeed = unitsFormatter.formatWindSpeed(hourWeather.windSpeed),
        humidity = unitsFormatter.formatHumidity(hourWeather.humidity),
        visibility = unitsFormatter.formatVisibility(hourWeather.visibility),
    )
}
