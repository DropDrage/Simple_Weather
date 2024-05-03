package com.dropdrage.simpleweather.core.presentation.utils

import android.content.Context
import com.dropdrage.simpleweather.core.presentation.R
import com.dropdrage.simpleweather.data.settings.CanBePluralUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class WeatherUnitsFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    val noTemperature = context.getString(
        WeatherUnitsPreferences.temperatureUnit.unitResId,
        NO_VALUE,
    )


    fun formatTemperature(value: Float): String =
        formatTemperature(value, WeatherUnitsPreferences.temperatureUnit)

    private fun formatTemperature(value: Float, unit: WeatherUnit): String = formatUnit(value, unit)

    fun formatPressure(value: Int): String =
        formatPressure(value, WeatherUnitsPreferences.pressureUnit)

    private fun formatPressure(value: Int, unit: WeatherUnit): String = formatUnit(value, unit)

    fun formatHumidity(value: Int): String =
        context.getString(R.string.weather_unit_humidity, value.toString())

    fun formatWindSpeed(value: Float): String =
        formatWindSpeed(value, WeatherUnitsPreferences.windSpeedUnit)

    private fun formatWindSpeed(value: Float, unit: CanBePluralUnit): String = formatPlural(value.toInt(), unit)

    fun formatVisibility(value: Float): String =
        formatVisibility(value, WeatherUnitsPreferences.visibilityUnit)

    private fun formatVisibility(value: Float, unit: CanBePluralUnit): String = formatPlural(value, unit)

    fun formatPrecipitation(value: Float): String =
        formatPrecipitation(value, WeatherUnitsPreferences.precipitationUnit)

    private fun formatPrecipitation(value: Float, unit: CanBePluralUnit): String = formatPlural(value, unit)


    private fun formatUnit(value: Int, unit: WeatherUnit) =
        context.getString(unit.unitResId, value.toString())

    private fun formatUnit(value: Float, unit: WeatherUnit) =
        context.getString(unit.unitResId, fastFormat(value))

    private fun formatPlural(value: Int, unit: CanBePluralUnit): String {
        val valueString = value.toString()
        return if (unit.isNotPlural) context.getString(unit.unitResId, valueString)
        else context.resources.getQuantityString(unit.unitPluralResId, value, valueString)
    }

    private fun formatPlural(value: Float, unit: CanBePluralUnit): String {
        val valueString = fastFormat(value)
        return if (unit.isNotPlural) context.getString(unit.unitResId, valueString)
        else context.resources.getQuantityString(unit.unitPluralResId, value.toInt(), valueString)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun fastFormat(value: Float): String {
        val valueString = value.toString()
        return valueString.take(valueString.indexOf(FLOAT_DOT) + 2)
    }


    @OptIn(ExperimentalContracts::class)
    fun <T> bulkFormat(block: BulkWeatherUnitsFormatter.() -> T): T {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        return BulkWeatherUnitsFormatter().run(block)
    }

    inner class BulkWeatherUnitsFormatter {

        private val temperatureUnit = WeatherUnitsPreferences.temperatureUnit
        private val pressureUnit = WeatherUnitsPreferences.pressureUnit
        private val windSpeedUnit = WeatherUnitsPreferences.windSpeedUnit
        private val visibilityUnit = WeatherUnitsPreferences.visibilityUnit
        private val precipitationUnit = WeatherUnitsPreferences.precipitationUnit


        fun formatTemperature(value: Float): String = formatTemperature(value, temperatureUnit)

        fun formatPressure(value: Int): String = formatPressure(value, pressureUnit)

        fun formatHumidity(value: Int): String = this@WeatherUnitsFormatter.formatHumidity(value)

        fun formatWindSpeed(value: Float): String = formatWindSpeed(value, windSpeedUnit)

        fun formatVisibility(value: Float): String = formatVisibility(value, visibilityUnit)

        fun formatPrecipitation(value: Float): String = formatPrecipitation(value, precipitationUnit)

    }


    companion object {
        private const val FLOAT_DOT = '.'

        private const val NO_VALUE = "--"
    }

}
