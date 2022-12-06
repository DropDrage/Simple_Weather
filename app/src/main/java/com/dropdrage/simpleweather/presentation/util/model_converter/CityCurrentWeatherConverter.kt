package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.domain.weather.WeatherType
import com.dropdrage.simpleweather.domain.weather.current.CityCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import javax.inject.Inject

class CityCurrentWeatherConverter @Inject constructor(
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(cityCurrentWeather: CityCurrentWeather): ViewCityCurrentWeather {
        val currentTemperature: String
        val weatherType: ViewWeatherType
        if (cityCurrentWeather.weather != null) {
            currentTemperature = unitsFormatter.formatTemperature(cityCurrentWeather.weather.temperature)
            weatherType = ViewWeatherType.fromWeatherType(cityCurrentWeather.weather.weatherType)
        } else {
            currentTemperature = unitsFormatter.noTemperature
            weatherType = ViewWeatherType.fromWeatherType(WeatherType.ClearSky)
        }

        return ViewCityCurrentWeather(
            city = cityCurrentWeather.city,
            currentWeather = ViewCurrentWeather(currentTemperature, weatherType)
        )
    }
}