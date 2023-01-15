package com.dropdrage.simpleweather.data.source.local.util

import com.dropdrage.simpleweather.settings.data.PrecipitationUnit
import com.dropdrage.simpleweather.settings.data.PressureUnit
import com.dropdrage.simpleweather.settings.data.TemperatureUnit
import com.dropdrage.simpleweather.settings.data.VisibilityUnit
import com.dropdrage.simpleweather.settings.data.WeatherUnitsPreferences
import com.dropdrage.simpleweather.settings.data.WindSpeedUnit
import com.dropdrage.simpleweather.settings.data.utils.ApiSupportedUnits

private const val CELSIUS_TO_FAHRENHEIT_MODIFIER = 9 / 5f
private const val CELSIUS_TO_FAHRENHEIT_OFFSET = 32

private const val HPA_TO_MM_HG_MODIFIER = 0.7500616f

private const val KM_H_TO_MPH_DIVIDER = 1.6f
private const val KM_H_TO_M_S_DIVIDER = 3.6f
private const val KM_H_TO_M_S_MULTIPLIER = 0.54f

private const val METER_TO_KM_DIVIDER = 1000f
private const val METER_TO_MILE_DIVIDER = 1609f

private const val MM_TO_INCH_DIVIDER = 25.4f

object WeatherUnitsDeconverter {

    private fun deconvertTemperature(temperatureCelsius: Float): Float =
        when (WeatherUnitsPreferences.temperatureUnit) {
            TemperatureUnit.CELSIUS -> temperatureCelsius
            TemperatureUnit.FAHRENHEIT -> (temperatureCelsius - CELSIUS_TO_FAHRENHEIT_OFFSET) /
                CELSIUS_TO_FAHRENHEIT_MODIFIER
        }

    private fun deconvertPressure(pressureHpa: Int): Int =
        when (WeatherUnitsPreferences.pressureUnit) {
            PressureUnit.H_PASCAL -> pressureHpa
            PressureUnit.MM_HG -> (pressureHpa / HPA_TO_MM_HG_MODIFIER).toInt()
        }

    private fun deconvertWindSpeed(windSpeedKmh: Float): Float =
        when (WeatherUnitsPreferences.windSpeedUnit) {
            WindSpeedUnit.KM_H -> windSpeedKmh
            WindSpeedUnit.MPH -> windSpeedKmh * KM_H_TO_MPH_DIVIDER
            WindSpeedUnit.M_S -> windSpeedKmh * KM_H_TO_M_S_DIVIDER
            WindSpeedUnit.KNOTS -> windSpeedKmh / KM_H_TO_M_S_MULTIPLIER
        }

    private fun deconvertVisibility(visibilityMeters: Float): Float =
        when (WeatherUnitsPreferences.visibilityUnit) {
            VisibilityUnit.METER -> visibilityMeters
            VisibilityUnit.K_METER -> visibilityMeters * METER_TO_KM_DIVIDER
            VisibilityUnit.MILE -> visibilityMeters * METER_TO_MILE_DIVIDER
        }

    private fun deconvertPrecipitation(precipitationMm: Float): Float =
        when (WeatherUnitsPreferences.precipitationUnit) {
            PrecipitationUnit.MM -> precipitationMm
            PrecipitationUnit.INCH -> precipitationMm * MM_TO_INCH_DIVIDER
        }


    fun deconvertTemperatureIfApiSupport(value: Float) = deconvertIfApiSupport(
        value,
        ApiSupportedUnits.isTemperatureSupported,
        WeatherUnitsDeconverter::deconvertTemperature
    )

    fun deconvertPressureIfApiSupport(value: Int) = deconvertIfApiSupport(
        value,
        ApiSupportedUnits.isPressureSupported,
        WeatherUnitsDeconverter::deconvertPressure
    )

    fun deconvertWindSpeedIfApiSupport(value: Float) = deconvertIfApiSupport(
        value,
        ApiSupportedUnits.isWindSpeedSupported,
        WeatherUnitsDeconverter::deconvertWindSpeed
    )

    fun deconvertVisibilityIfApiSupport(value: Float): Float = deconvertIfApiSupport(
        value,
        ApiSupportedUnits.isVisibilitySupported,
        WeatherUnitsDeconverter::deconvertVisibility
    )

    fun deconvertPrecipitationIfApiSupport(value: Float) = deconvertIfApiSupport(
        value,
        ApiSupportedUnits.isPrecipitationSupported,
        WeatherUnitsDeconverter::deconvertPrecipitation
    )

    private inline fun <T : Number> deconvertIfApiSupport(value: T, isSupported: Boolean, convert: (T) -> T): T =
        if (isSupported) convert(value)
        else value

}
