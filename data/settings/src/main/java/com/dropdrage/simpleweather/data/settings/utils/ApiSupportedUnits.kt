package com.dropdrage.simpleweather.data.settings.utils

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit

object ApiSupportedUnits {
    val isTemperatureSupported = TemperatureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPressureSupported = PressureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isWindSpeedSupported = WindSpeedUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isVisibilitySupported = VisibilityUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPrecipitationSupported = PrecipitationUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
}