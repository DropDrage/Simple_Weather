package com.dropdrage.simpleweather.data.weather.local.cache.util

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.settings.util.ApiSupportedUnits
import com.dropdrage.simpleweather.data.weather.remote.utils.ApiUnits

internal object CacheUnits {
    val TEMPERATURE = TemperatureUnit.CELSIUS
    val PRESSURE = PressureUnit.H_PASCAL
    val WIND_SPEED = WindSpeedUnit.M_S
    val VISIBILITY = VisibilityUnit.METER
    val PRECIPITATION = PrecipitationUnit.MM


    val isTemperatureConversionNeeded =
        ApiSupportedUnits.isTemperatureSupported || TEMPERATURE == ApiUnits.TEMPERATURE

    val isPressureConversionNeeded =
        ApiSupportedUnits.isPressureSupported || PRESSURE == ApiUnits.PRESSURE

    val isWindSpeedConversionNeeded =
        ApiSupportedUnits.isWindSpeedSupported || WIND_SPEED == ApiUnits.WIND_SPEED

    val isVisibilityConversionNeeded =
        ApiSupportedUnits.isVisibilitySupported || VISIBILITY == ApiUnits.VISIBILITY

    val isPrecipitationConversionNeeded =
        ApiSupportedUnits.isPrecipitationSupported || PRECIPITATION == ApiUnits.PRECIPITATION
}
