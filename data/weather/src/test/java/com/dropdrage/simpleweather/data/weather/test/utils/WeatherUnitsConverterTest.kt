package com.dropdrage.simpleweather.data.weather.test.utils

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.settings.util.ApiSupportedUnits
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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class WeatherUnitsConverterTest {

    @Nested
    inner class Temperature {

        @Nested
        inner class convertTemperatureIfApiDontSupport {

            @Test
            fun `supported then returns same value`() {
                every { ApiSupportedUnits.isTemperatureSupported } returns true
                val value = 124f

                val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

                assertEquals(value, result)
            }

            @Test
            fun `not supported and Preferences Fahrenheit`() {
                every { ApiSupportedUnits.isTemperatureSupported } returns false
                every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.FAHRENHEIT
                val value = 124f
                val valueConverted = value * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET

                val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences Celsius then returns same value`() {
                every { ApiSupportedUnits.isTemperatureSupported } returns false
                every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS
                val value = 124f

                val result = WeatherUnitsConverter.convertTemperatureIfApiDontSupport(value)

                assertEquals(value, result)
            }

        }

        @Nested
        inner class convertTemperature {

            @Test
            fun `Preferences Fahrenheit`() {
                every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.FAHRENHEIT
                val value = 124f
                val valueConverted = value * CELSIUS_TO_FAHRENHEIT_MODIFIER + CELSIUS_TO_FAHRENHEIT_OFFSET

                val result = WeatherUnitsConverter.convertTemperature(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences Celsius then returns same value`() {
                every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS
                val value = 124f

                val result = WeatherUnitsConverter.convertTemperature(value)

                assertEquals(value, result)
            }

        }

    }

    @Nested
    inner class Pressure {

        @Nested
        inner class convertTemperatureIfApiDontSupport {

            @Test
            fun `supported then returns same value`() {
                every { ApiSupportedUnits.isPressureSupported } returns true
                val value = 124f
                val valueConverted = value.toInt()

                val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences MM_HG`() {
                every { ApiSupportedUnits.isPressureSupported } returns false
                every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.MM_HG
                val value = 124f
                val valueConverted = (value * WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER).toInt()

                val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences H_PASCAL then returns same value`() {
                every { ApiSupportedUnits.isPressureSupported } returns false
                every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.H_PASCAL
                val value = 124f
                val valueConverted = value.toInt()

                val result = WeatherUnitsConverter.convertPressureIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

        }

        @Nested
        inner class convertPressure {

            @Test
            fun `Preferences MM_HG`() {
                every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.MM_HG
                val value = 124
                val valueConverted = (value * WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER).toInt()

                val result = WeatherUnitsConverter.convertPressure(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences H_PASCAL then returns same value`() {
                every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.H_PASCAL
                val value = 124

                val result = WeatherUnitsConverter.convertPressure(value)

                assertEquals(value, result)
            }

        }

    }

    @Nested
    inner class WindSpeed {

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


        @Nested
        inner class convertWindSpeed {

            @Test
            fun `Preferences MPH`() {
                every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.MPH
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER

                val result = WeatherUnitsConverter.convertWindSpeed(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences M_S`() {
                every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.M_S
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER

                val result = WeatherUnitsConverter.convertWindSpeed(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences KNOTS`() {
                every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KNOTS
                val value = 124f
                val valueConverted = value * WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER

                val result = WeatherUnitsConverter.convertWindSpeed(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences KM_H then returns same value`() {
                every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KM_H
                val value = 124f

                val result = WeatherUnitsConverter.convertWindSpeed(value)

                assertEquals(value, result)
            }

        }

    }

    @Nested
    inner class Visibility {

        @Nested
        inner class convertVisibilityIfApiDontSupport {

            @Test
            fun `supported then returns same value`() {
                every { ApiSupportedUnits.isVisibilitySupported } returns true
                val value = 124

                val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

                assertEquals(value.toFloat(), result)
            }

            @Test
            fun `not supported and Preferences K_METER`() {
                every { ApiSupportedUnits.isVisibilitySupported } returns false
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.K_METER
                val value = 124
                val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER

                val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences MILE`() {
                every { ApiSupportedUnits.isVisibilitySupported } returns false
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.MILE
                val value = 124
                val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER

                val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences METER then returns same value`() {
                every { ApiSupportedUnits.isVisibilitySupported } returns false
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.METER
                val value = 124

                val result = WeatherUnitsConverter.convertVisibilityIfApiDontSupport(value)

                assertEquals(value.toFloat(), result)
            }

        }

        @Nested
        inner class convertVisibility {

            @Test
            fun `Preferences K_METER`() {
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.K_METER
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER

                val result = WeatherUnitsConverter.convertVisibility(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences MILE`() {
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.MILE
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER

                val result = WeatherUnitsConverter.convertVisibility(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences METER then returns same value`() {
                every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.METER
                val value = 124f

                val result = WeatherUnitsConverter.convertVisibility(value)

                assertEquals(value, result)
            }

        }

    }

    @Nested
    inner class Precipitation {

        @Nested
        inner class convertPrecipitationIfApiDontSupport {

            @Test
            fun `supported then returns same value`() {
                every { ApiSupportedUnits.isPrecipitationSupported } returns true
                val value = 124f

                val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

                assertEquals(value, result)
            }

            @Test
            fun `not supported and Preferences INCH`() {
                every { ApiSupportedUnits.isPrecipitationSupported } returns false
                every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.INCH
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

                val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `not supported and Preferences MM then returns same value`() {
                every { ApiSupportedUnits.isPrecipitationSupported } returns false
                every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.MM
                val value = 124f

                val result = WeatherUnitsConverter.convertPrecipitationIfApiDontSupport(value)

                assertEquals(value, result)
            }

        }

        @Nested
        inner class convertPrecipitation {

            @Test
            fun `Preferences INCH`() {
                every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.INCH
                val value = 124f
                val valueConverted = value / WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER

                val result = WeatherUnitsConverter.convertPrecipitation(value)

                assertEquals(valueConverted, result)
            }

            @Test
            fun `Preferences MM then returns same value`() {
                every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.MM
                val value = 124f

                val result = WeatherUnitsConverter.convertPrecipitation(value)

                assertEquals(value, result)
            }

        }

    }


    private companion object {

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            mockkObject(ApiSupportedUnits, WeatherUnitsPreferences)
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            unmockkObject(ApiSupportedUnits, WeatherUnitsPreferences)
        }

    }

}