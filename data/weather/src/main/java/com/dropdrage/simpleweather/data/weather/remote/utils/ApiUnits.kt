package com.dropdrage.simpleweather.data.weather.remote.utils

import com.dropdrage.simpleweather.settings.PrecipitationUnit
import com.dropdrage.simpleweather.settings.PressureUnit
import com.dropdrage.simpleweather.settings.TemperatureUnit
import com.dropdrage.simpleweather.settings.VisibilityUnit
import com.dropdrage.simpleweather.settings.WindSpeedUnit

@Suppress("RedundantNullableReturnType")
internal object ApiUnits {
    val API_SUPPORTED_UNIT = null


    val TEMPERATURE: TemperatureUnit? = API_SUPPORTED_UNIT
    val PRESSURE: PressureUnit? = PressureUnit.H_PASCAL
    val WIND_SPEED: WindSpeedUnit? = API_SUPPORTED_UNIT
    val VISIBILITY: VisibilityUnit? = VisibilityUnit.METER
    val PRECIPITATION: PrecipitationUnit? = API_SUPPORTED_UNIT
}
