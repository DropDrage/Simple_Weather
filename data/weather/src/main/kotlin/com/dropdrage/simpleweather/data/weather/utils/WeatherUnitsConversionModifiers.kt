package com.dropdrage.simpleweather.data.weather.utils

internal object WeatherUnitsConversionModifiers {
    const val CELSIUS_TO_FAHRENHEIT_MODIFIER = 9 / 5f
    const val CELSIUS_TO_FAHRENHEIT_OFFSET = 32

    const val HPA_TO_MM_HG_MODIFIER = 0.7500616f

    const val KM_H_TO_MPH_DIVIDER = 1.6f
    const val KM_H_TO_M_S_DIVIDER = 3.6f
    const val KM_H_TO_M_S_MULTIPLIER = 0.54f

    const val METER_TO_KM_DIVIDER = 1000f
    const val METER_TO_MILE_DIVIDER = 1609f

    const val MM_TO_INCH_DIVIDER = 25.4f
}
