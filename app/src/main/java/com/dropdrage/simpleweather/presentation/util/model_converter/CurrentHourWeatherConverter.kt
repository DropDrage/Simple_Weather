package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.data.weather.domain.HourWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentHourWeather
import javax.inject.Inject

class CurrentHourWeatherConverter @Inject constructor(private val unitsFormatter: WeatherUnitsFormatter) {
    fun convertToView(hourWeather: HourWeather) = ViewCurrentHourWeather(
        weatherType = ViewWeatherType.fromWeatherType(hourWeather.weatherType),
        temperature = unitsFormatter.formatTemperature(hourWeather.temperature),
        pressure = unitsFormatter.formatPressure(hourWeather.pressure),
        windSpeed = unitsFormatter.formatWindSpeed(hourWeather.windSpeed),
        humidity = unitsFormatter.formatHumidity(hourWeather.humidity),
        visibility = unitsFormatter.formatVisibility(hourWeather.visibility),
    )
}
