package com.dropdrage.simpleweather.data.preferences

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import com.dropdrage.simpleweather.data.util.isLocaleMetric

object WeatherUnitsPreferences : KotprefModel() {
    var temperatureUnit: TemperatureUnit by enumOrdinalPref(
        if (isLocaleMetric()) TemperatureUnit.CELSIUS else TemperatureUnit.FAHRENHEIT,
    )

    var pressureUnit: PressureUnit by enumOrdinalPref(
        if (isLocaleMetric()) PressureUnit.MM_HG else PressureUnit.H_PASCAL
    )

    var windSpeedUnit: WindSpeedUnit by enumOrdinalPref(
        if (isLocaleMetric()) WindSpeedUnit.KM_H else WindSpeedUnit.MPH
    )

    var visibilityUnit: VisibilityUnit by enumOrdinalPref(
        if (isLocaleMetric()) VisibilityUnit.K_METER else VisibilityUnit.MILE
    )

    var precipitationUnit: PrecipitationUnit by enumOrdinalPref(
        if (isLocaleMetric()) PrecipitationUnit.MM else PrecipitationUnit.INCH
    )
}