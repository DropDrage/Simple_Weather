package com.dropdrage.simpleweather.settings_data.utils

import com.dropdrage.simpleweather.settings_data.*

object ApiSupportedUnits {
    val isTemperatureSupported = TemperatureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPressureSupported = PressureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isWindSpeedSupported = WindSpeedUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isVisibilitySupported = VisibilityUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPrecipitationSupported = PrecipitationUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
}