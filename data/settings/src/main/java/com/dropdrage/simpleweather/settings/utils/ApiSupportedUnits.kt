package com.dropdrage.simpleweather.settings.utils

import com.dropdrage.simpleweather.settings.PrecipitationUnit
import com.dropdrage.simpleweather.settings.PressureUnit
import com.dropdrage.simpleweather.settings.TemperatureUnit
import com.dropdrage.simpleweather.settings.VisibilityUnit
import com.dropdrage.simpleweather.settings.WindSpeedUnit

object ApiSupportedUnits {
    val isTemperatureSupported = TemperatureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPressureSupported = PressureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isWindSpeedSupported = WindSpeedUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isVisibilitySupported = VisibilityUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPrecipitationSupported = PrecipitationUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
}