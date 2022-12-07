package com.dropdrage.simpleweather.presentation.util.format

import android.content.Context
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.data.preferences.CanBePluralUnit
import com.dropdrage.simpleweather.data.preferences.WeatherUnit
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FLOAT_FORMAT = "%.1f"

private const val NO_VALUE = "--"

class WeatherUnitsFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    val noTemperature = context.getString(WeatherUnitsPreferences.temperatureUnit.unitResId, NO_VALUE)


    fun formatTemperature(value: Float): String = formatUnit(value, WeatherUnitsPreferences.temperatureUnit)

    fun formatPressure(value: Int): String = formatUnit(value, WeatherUnitsPreferences.pressureUnit)

    fun formatHumidity(value: Int): String =
        context.getString(R.string.weather_unit_humidity, value.toString())

    fun formatWindSpeed(value: Float): String = formatPlural(value, WeatherUnitsPreferences.windSpeedUnit)

    fun formatVisibility(value: Float): String = formatPlural(value, WeatherUnitsPreferences.visibilityUnit)

    fun formatPrecipitation(value: Float): String = formatPlural(value, WeatherUnitsPreferences.precipitationUnit)


    private fun formatUnit(value: Int, unit: WeatherUnit) =
        context.getString(unit.unitResId, value.toString())

    private fun formatUnit(value: Float, unit: WeatherUnit) =
        context.getString(unit.unitResId, String.format(FLOAT_FORMAT, value))

    private fun formatPlural(value: Float, unit: CanBePluralUnit): String {
        val valueString = String.format(FLOAT_FORMAT, value)
        return if (!unit.isPlural) context.getString(unit.unitResId, valueString)
        else context.resources.getQuantityString(unit.unitPluralResId, value.toInt(), valueString)
    }
}