package com.dropdrage.simpleweather.data.weather.local.cache.util.converter

import android.util.Log
import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.weather.local.cache.util.CacheUnits
import com.dropdrage.simpleweather.data.weather.remote.utils.ApiUnits
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_OFFSET
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

private const val TAG = "Deconverter"

internal object WeatherUnitsDeconverter {

    fun deconvertTemperatureIfApiDontSupport(value: Float) = deconvertIfApiDontSupportCacheUnit(
        value,
        CacheUnits.isTemperatureConversionNeeded,
        WeatherUnitsDeconverter::deconvertTemperature
    )

    private fun deconvertTemperature(value: Float): Float = when (ApiUnits.TEMPERATURE) {
        TemperatureUnit.FAHRENHEIT -> (value - CELSIUS_TO_FAHRENHEIT_OFFSET) /
            CELSIUS_TO_FAHRENHEIT_MODIFIER
        ApiUnits.API_SUPPORTED_UNIT ->
            throw IllegalStateException("Unit is supported by api. Deconversion not needed or change ApiUnits")
        else -> {
            Log.w(TAG, "Api unit is equals to cache unit")
            value
        }
    }


    fun deconvertPressureIfApiDontSupport(value: Int) = deconvertIfApiDontSupportCacheUnit(
        value,
        CacheUnits.isPressureConversionNeeded,
        WeatherUnitsDeconverter::deconvertPressure
    )

    private fun deconvertPressure(value: Int): Int = when (ApiUnits.PRESSURE) {
        PressureUnit.MM_HG -> (value / HPA_TO_MM_HG_MODIFIER).toInt()
        ApiUnits.API_SUPPORTED_UNIT ->
            throw IllegalStateException("Unit is supported by api. Deconversion not needed or change ApiUnits")
        else -> {
            Log.w(TAG, "Api unit is equals to cache unit")
            value
        }
    }


    fun deconvertWindSpeedIfApiDontSupport(value: Float) = deconvertIfApiDontSupportCacheUnit(
        value,
        CacheUnits.isWindSpeedConversionNeeded,
        WeatherUnitsDeconverter::deconvertWindSpeed
    )

    private fun deconvertWindSpeed(value: Float): Float = when (ApiUnits.WIND_SPEED) {
        WindSpeedUnit.MPH -> value * KM_H_TO_MPH_DIVIDER
        WindSpeedUnit.M_S -> value * KM_H_TO_M_S_DIVIDER
        WindSpeedUnit.KNOTS -> value / KM_H_TO_M_S_MULTIPLIER
        ApiUnits.API_SUPPORTED_UNIT ->
            throw IllegalStateException("Unit is supported by api. Deconversion not needed or change ApiUnits")
        else -> {
            Log.w(TAG, "Api unit is equals to cache unit")
            value
        }
    }


    fun deconvertVisibilityIfApiDontSupport(value: Float): Float = deconvertIfApiDontSupportCacheUnit(
        value,
        CacheUnits.isVisibilityConversionNeeded,
        WeatherUnitsDeconverter::deconvertVisibility
    )

    private fun deconvertVisibility(value: Float): Float = when (ApiUnits.VISIBILITY) {
        VisibilityUnit.K_METER -> value * METER_TO_KM_DIVIDER
        VisibilityUnit.MILE -> value * METER_TO_MILE_DIVIDER
        ApiUnits.API_SUPPORTED_UNIT ->
            throw IllegalStateException("Unit is supported by api. Deconversion not needed or change ApiUnits")
        else -> {
            Log.w(TAG, "Api unit is equals to cache unit")
            value
        }
    }


    fun deconvertPrecipitationIfApiDontSupport(value: Float) = deconvertIfApiDontSupportCacheUnit(
        value,
        CacheUnits.isPrecipitationConversionNeeded,
        WeatherUnitsDeconverter::deconvertPrecipitation
    )

    private fun deconvertPrecipitation(value: Float): Float = when (ApiUnits.PRECIPITATION) {
        PrecipitationUnit.INCH -> value * MM_TO_INCH_DIVIDER
        ApiUnits.API_SUPPORTED_UNIT ->
            throw IllegalStateException("Unit is supported by API. Deconversion not needed or change ApiUnits")
        else -> {
            Log.w(TAG, "Api unit is equals to cache unit")
            value
        }
    }


    private inline fun <T : Number> deconvertIfApiDontSupportCacheUnit(
        value: T,
        isSupported: Boolean,
        deconvert: (T) -> T,
    ): T = if (isSupported) value else deconvert(value)

}
