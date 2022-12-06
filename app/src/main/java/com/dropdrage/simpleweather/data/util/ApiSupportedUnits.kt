package com.dropdrage.simpleweather.data.util

import com.dropdrage.simpleweather.data.preferences.PrecipitationUnit
import com.dropdrage.simpleweather.data.preferences.PressureUnit
import com.dropdrage.simpleweather.data.preferences.TemperatureUnit
import com.dropdrage.simpleweather.data.preferences.VisibilityUnit
import com.dropdrage.simpleweather.data.preferences.WindSpeedUnit

object ApiSupportedUnits {
    val isTemperatureSupported = TemperatureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPressureSupported = PressureUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isWindSpeedSupported = WindSpeedUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isVisibilitySupported = VisibilityUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
    val isPrecipitationSupported = PrecipitationUnit::class.java.interfaces.contains(ApiSupportedParam::class.java)
}