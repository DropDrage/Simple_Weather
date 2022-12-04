package com.dropdrage.simpleweather.presentation.util.model_converter

import com.dropdrage.simpleweather.domain.weather.CityCurrentWeather
import com.dropdrage.simpleweather.domain.weather.WeatherType
import com.dropdrage.simpleweather.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsConverter
import com.dropdrage.simpleweather.presentation.util.format.WeatherUnitsFormatter
import javax.inject.Inject

class CityCurrentWeatherConverter @Inject constructor(
    private val unitsConverter: WeatherUnitsConverter,
    private val unitsFormatter: WeatherUnitsFormatter,
) {
    fun convertToView(cityCurrentWeather: CityCurrentWeather): ViewCityCurrentWeather {
        val currentTemperature: String
        val weatherType: ViewWeatherType
        if (cityCurrentWeather.weather != null) {
            currentTemperature = unitsFormatter.formatTemperature(unitsConverter.convertTemperature(
                cityCurrentWeather.weather.temperatureCelsius
            ))
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