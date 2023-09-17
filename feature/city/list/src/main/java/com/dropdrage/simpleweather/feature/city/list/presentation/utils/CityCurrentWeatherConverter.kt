package com.dropdrage.simpleweather.feature.city.list.presentation.utils

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.city.list.domain.CityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCurrentWeather
import javax.inject.Inject

internal class CityCurrentWeatherConverter @Inject constructor(
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
