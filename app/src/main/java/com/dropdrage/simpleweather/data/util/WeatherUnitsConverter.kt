package com.dropdrage.simpleweather.data.util

import com.dropdrage.simpleweather.data.preferences.PrecipitationUnit
import com.dropdrage.simpleweather.data.preferences.PressureUnit
import com.dropdrage.simpleweather.data.preferences.TemperatureUnit
import com.dropdrage.simpleweather.data.preferences.VisibilityUnit
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.preferences.WindSpeedUnit

private const val CELSIUS_TO_FAHRENHEIT_MODIFIER = 9 / 5f
private const val CELSIUS_TO_FAHRENHEIT_OFFSET = 32

private const val HPA_TO_MM_HG_MODIFIER = 0.7500616f

private const val KM_H_TO_MPH_DIVIDER = 1.6f
private const val KM_H_TO_M_S_DIVIDER = 3.6f
private const val KM_H_TO_M_S_MULTIPLIER = 0.54f

private const val METER_TO_KM_DIVIDER = 1000
private const val METER_TO_MILE_DIVIDER = 1609

private const val MM_TO_INCH_DIVIDER = 25.4f

object WeatherUnitsConverter {
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

    fun convertVisibility(visibilityMeters: Int) = when (WeatherUnitsPreferences.visibilityUnit) {
        VisibilityUnit.METER -> visibilityMeters
        VisibilityUnit.K_METER -> visibilityMeters / METER_TO_KM_DIVIDER
        VisibilityUnit.MILE -> visibilityMeters / METER_TO_MILE_DIVIDER
    }

    fun convertPrecipitation(precipitationMm: Float) = when (WeatherUnitsPreferences.precipitationUnit) {
        PrecipitationUnit.MM -> precipitationMm
        PrecipitationUnit.INCH -> precipitationMm / MM_TO_INCH_DIVIDER
    }


    fun convertTemperatureIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isTemperatureSupported,
        ::convertTemperature
    )

    fun convertPressureIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isPressureSupported,
        ::convertPressure
    )

    fun convertWindSpeedIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isWindSpeedSupported,
        ::convertWindSpeed
    )

    fun convertVisibilityIfApiDontSupport(value: Int) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isVisibilitySupported,
        ::convertVisibility
    )

    fun convertPrecipitationIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isPrecipitationSupported,
        ::convertPrecipitation
    )

    private fun <T> convertIfApiDontSupport(value: T, isSupported: Boolean, convert: (T) -> T): T =
        if (isSupported) value
        else convert(value)
}