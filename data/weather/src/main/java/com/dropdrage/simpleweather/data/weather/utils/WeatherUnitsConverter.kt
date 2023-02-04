package com.dropdrage.simpleweather.data.weather.utils

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.settings.utils.ApiSupportedUnits
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_OFFSET
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

internal object WeatherUnitsConverter {

    fun convertTemperatureIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isTemperatureSupported,
        ::convertTemperature
    )

    fun convertTemperature(temperatureCelsius: Float): Float = when (WeatherUnitsPreferences.temperatureUnit) {
        TemperatureUnit.CELSIUS -> temperatureCelsius
        TemperatureUnit.FAHRENHEIT -> temperatureCelsius * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET
    }


    fun convertPressureIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value.toInt(),
        ApiSupportedUnits.isPressureSupported,
        ::convertPressure
    )

    fun convertPressure(pressureHpa: Int): Int = when (WeatherUnitsPreferences.pressureUnit) {
        PressureUnit.H_PASCAL -> pressureHpa
        PressureUnit.MM_HG -> (pressureHpa * HPA_TO_MM_HG_MODIFIER).toInt()
    }


    fun convertWindSpeedIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isWindSpeedSupported,
        ::convertWindSpeed
    )

    fun convertWindSpeed(windSpeedKmh: Float): Float = when (WeatherUnitsPreferences.windSpeedUnit) {
        WindSpeedUnit.KM_H -> windSpeedKmh
        WindSpeedUnit.MPH -> windSpeedKmh / KM_H_TO_MPH_DIVIDER
        WindSpeedUnit.M_S -> windSpeedKmh / KM_H_TO_M_S_DIVIDER
        WindSpeedUnit.KNOTS -> windSpeedKmh * KM_H_TO_M_S_MULTIPLIER
    }


    fun convertVisibilityIfApiDontSupport(value: Int): Float = convertIfApiDontSupport(
        value.toFloat(),
        ApiSupportedUnits.isVisibilitySupported,
        ::convertVisibility
    )

    fun convertVisibility(visibilityMeters: Float): Float = when (WeatherUnitsPreferences.visibilityUnit) {
        VisibilityUnit.METER -> visibilityMeters
        VisibilityUnit.K_METER -> visibilityMeters / METER_TO_KM_DIVIDER
        VisibilityUnit.MILE -> visibilityMeters / METER_TO_MILE_DIVIDER
    }


    fun convertPrecipitationIfApiDontSupport(value: Float) = convertIfApiDontSupport(
        value,
        ApiSupportedUnits.isPrecipitationSupported,
        ::convertPrecipitation
    )

    fun convertPrecipitation(precipitationMm: Float): Float = when (WeatherUnitsPreferences.precipitationUnit) {
        PrecipitationUnit.MM -> precipitationMm
        PrecipitationUnit.INCH -> precipitationMm / MM_TO_INCH_DIVIDER
    }


    private inline fun <T : Number> convertIfApiDontSupport(value: T, isSupported: Boolean, convert: (T) -> T): T =
        if (isSupported) value
        else convert(value)

}
