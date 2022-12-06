package com.dropdrage.simpleweather.presentation.util.format

import com.dropdrage.simpleweather.data.preferences.PressureUnit
import com.dropdrage.simpleweather.data.preferences.TemperatureUnit
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.preferences.WindSpeedUnit
import javax.inject.Inject

private const val CELSIUS_TO_FAHRENHEIT_MODIFIER = 9 / 5f
private const val CELSIUS_TO_FAHRENHEIT_OFFSET = 32

private const val HPA_TO_MM_HG_MODIFIER = 0.75006157584566f

private const val KM_H_TO_MPH_DIVIDER = 1.6f
private const val KM_H_TO_M_S_DIVIDER = 3.6f
private const val KM_H_TO_M_S_MULTIPLIER = 0.54f

class WeatherUnitsConverter @Inject constructor() {
    fun convertTemperature(temperatureCelsius: Float) = when (WeatherUnitsPreferences.temperatureUnit) {
        TemperatureUnit.CELSIUS -> temperatureCelsius
        TemperatureUnit.FAHRENHEIT -> temperatureCelsius * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET
    }

    fun convertPressure(pressureHpa: Float) = when (WeatherUnitsPreferences.pressureUnit) {
        PressureUnit.H_PASCAL -> pressureHpa
        PressureUnit.MM_HG -> pressureHpa * HPA_TO_MM_HG_MODIFIER
    }

    fun convertWindSpeed(windSpeedKmh: Float) = when (WeatherUnitsPreferences.windSpeedUnit) {
        WindSpeedUnit.KM_H -> windSpeedKmh
        WindSpeedUnit.MPH -> windSpeedKmh / KM_H_TO_MPH_DIVIDER
        WindSpeedUnit.M_S -> windSpeedKmh / KM_H_TO_M_S_DIVIDER
        WindSpeedUnit.KNOTS -> windSpeedKmh * KM_H_TO_M_S_MULTIPLIER
    }
}