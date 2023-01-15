package com.dropdrage.simpleweather.settings.data.utils

import com.dropdrage.simpleweather.settings.data.PrecipitationUnit
import com.dropdrage.simpleweather.settings.data.PressureUnit
import com.dropdrage.simpleweather.settings.data.TemperatureUnit
import com.dropdrage.simpleweather.settings.data.VisibilityUnit
import com.dropdrage.simpleweather.settings.data.WindSpeedUnit

object ApiSupportedUnits {
    val isTemperatureSupported = TemperatureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPressureSupported = PressureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isWindSpeedSupported = WindSpeedUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isVisibilitySupported = VisibilityUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPrecipitationSupported = PrecipitationUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
}