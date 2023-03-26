package com.dropdrage.simpleweather.data.weather.test.utils

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.settings.utils.ApiSupportedUnits
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_OFFSET
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConverter
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

@Suppress("UnnecessaryVariable")
internal class WeatherUnitsConverterTest {

    //region Temperature

    @Test
    fun `convertTemperatureIfApiDontSupport supported`() {
        every { ApiSupportedUnits.isTemperatureSupported } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertTemperatureIfApiDontSupport not supported and Preferences Fahrenheit`() {
        every { ApiSupportedUnits.isTemperatureSupported } returns false
        every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.FAHRENHEIT
        val value = 124f
        val valueConverted = value * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET

        val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertTemperatureIfApiDontSupport not supported and Preferences Celsius`() {
        every { ApiSupportedUnits.isTemperatureSupported } returns false
        every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }


    @Test
    fun `convertTemperature not supported and Preferences Fahrenheit`() {
        every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.FAHRENHEIT
        val value = 124f
        val valueConverted = value * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET

        val result = WeatherUnitsConverter.convertTemperature(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertTemperature not supported and Preferences Celsius`() {
        every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertTemperature(value)

        assertEquals(valueConverted, result)
    }

    //endregion

    //region Pressure

    @Test
    fun `convertPressureIfApiDontSupport supported`() {
        every { ApiSupportedUnits.isPressureSupported } returns true
        val value = 124f
        val valueConverted = value.toInt()

        val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPressureIfApiDontSupport not supported and Preferences MM_HG`() {
        every { ApiSupportedUnits.isPressureSupported } returns false
        every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.MM_HG
        val value = 124f
        val valueConverted = (value * WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER).toInt()

        val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPressureIfApiDontSupport not supported and Preferences H_PASCAL`() {
        every { ApiSupportedUnits.isPressureSupported } returns false
        every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.H_PASCAL
        val value = 124f
        val valueConverted = value.toInt()

        val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }


    @Test
    fun `convertPressure not supported and Preferences MM_HG`() {
        every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.MM_HG
        val value = 124
        val valueConverted = (value * WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER).toInt()

        val result = WeatherUnitsConverter.convertPressure(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPressure not supported and Preferences H_PASCAL`() {
        every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.H_PASCAL
        val value = 124
        val valueConverted = value

        val result = WeatherUnitsConverter.convertPressure(value)

        assertEquals(valueConverted, result)
    }

    //endregion

    //region Wind Speed

    @Test
    fun `convertWindSpeedIfApiDontSupport supported`() {
        every { ApiSupportedUnits.isWindSpeedSupported } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeedIfApiDontSupport not supported and Preferences MPH`() {
        every { ApiSupportedUnits.isWindSpeedSupported } returns false
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.MPH
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER

        val result = WeatherUnitsConverter.convertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeedIfApiDontSupport not supported and Preferences M_S`() {
        every { ApiSupportedUnits.isWindSpeedSupported } returns false
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.M_S
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER

        val result = WeatherUnitsConverter.convertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeedIfApiDontSupport not supported and Preferences KNOTS`() {
        every { ApiSupportedUnits.isWindSpeedSupported } returns false
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KNOTS
        val value = 124f
        val valueConverted = value * WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER

        val result = WeatherUnitsConverter.convertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeedIfApiDontSupport not supported and Preferences KM_H`() {
        every { ApiSupportedUnits.isWindSpeedSupported } returns false
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KM_H
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }


    @Test
    fun `convertWindSpeed not supported and Preferences MPH`() {
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.MPH
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER

        val result = WeatherUnitsConverter.convertWindSpeed(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeed not supported and Preferences M_S`() {
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.M_S
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER

        val result = WeatherUnitsConverter.convertWindSpeed(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeed not supported and Preferences KNOTS`() {
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KNOTS
        val value = 124f
        val valueConverted = value * WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER

        val result = WeatherUnitsConverter.convertWindSpeed(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertWindSpeed not supported and Preferences KM_H`() {
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KM_H
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertWindSpeed(value)

        assertEquals(valueConverted, result)
    }

    //endregion

    //region Visibility

    @Test
    fun `convertVisibilityIfApiDontSupport supported`() {
        every { ApiSupportedUnits.isVisibilitySupported } returns true
        val value = 124
        val valueConverted = value.toFloat()

        val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertVisibilityIfApiDontSupport not supported and Preferences K_METER`() {
        every { ApiSupportedUnits.isVisibilitySupported } returns false
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.K_METER
        val value = 124
        val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER

        val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertVisibilityIfApiDontSupport not supported and Preferences MILE`() {
        every { ApiSupportedUnits.isVisibilitySupported } returns false
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.MILE
        val value = 124
        val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER

        val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertVisibilityIfApiDontSupport not supported and Preferences METER`() {
        every { ApiSupportedUnits.isVisibilitySupported } returns false
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.METER
        val value = 124
        val valueConverted = value.toFloat()

        val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }


    @Test
    fun `convertVisibility not supported and Preferences K_METER`() {
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.K_METER
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER

        val result = WeatherUnitsConverter.convertVisibility(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertVisibility not supported and Preferences MILE`() {
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.MILE
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER

        val result = WeatherUnitsConverter.convertVisibility(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertVisibility not supported and Preferences METER`() {
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.METER
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertVisibility(value)

        assertEquals(valueConverted, result)
    }

    //endregion

    //region Precipitation

    @Test
    fun `convertPrecipitationIfApiDontSupport supported`() {
        every { ApiSupportedUnits.isPrecipitationSupported } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPrecipitationIfApiDontSupport not supported and Preferences INCH`() {
        every { ApiSupportedUnits.isPrecipitationSupported } returns false
        every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.INCH
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

        val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPrecipitationIfApiDontSupport not supported and Preferences MM`() {
        every { ApiSupportedUnits.isPrecipitationSupported } returns false
        every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.MM
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }


    @Test
    fun `convertPrecipitation not supported and Preferences INCH`() {
        every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.INCH
        val value = 124f
        val valueConverted = value / WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

        val result = WeatherUnitsConverter.convertPrecipitation(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `convertPrecipitation not supported and Preferences MM`() {
        every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.MM
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsConverter.convertPrecipitation(value)

        assertEquals(valueConverted, result)
    }

    //endregion


    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            mockkObject(ApiSupportedUnits)
            mockkObject(WeatherUnitsPreferences)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            unmockkObject(WeatherUnitsPreferences)
            unmockkObject(ApiSupportedUnits)
        }
    }

}