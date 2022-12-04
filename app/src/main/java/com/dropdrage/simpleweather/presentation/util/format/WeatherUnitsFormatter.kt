package com.dropdrage.simpleweather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FLOAT_FORMAT = "%.1f"

private const val NO_VALUE = "--"

class WeatherUnitsFormatter @Inject constructor(@ApplicationContext private val context: Context) {
    val noTemperature = context.getString(WeatherUnitsPreferences.temperatureUnit.unitResId, NO_VALUE)


    fun formatTemperature(valueCelsius: Float): String =
        context.getString(WeatherUnitsPreferences.temperatureUnit.unitResId, String.format(FLOAT_FORMAT, valueCelsius))

    fun formatPressure(value: Float): String =
        context.getString(WeatherUnitsPreferences.pressureUnit.unitResId, value.toInt())

    fun formatWindSpeed(value: Float): String =
        context.getString(WeatherUnitsPreferences.windSpeedUnit.unitResId, String.format(FLOAT_FORMAT, value))

    fun formatHumidity(value: Int): String =
        context.getString(R.string.weather_unit_humidity, value)
}