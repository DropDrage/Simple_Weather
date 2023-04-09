package com.dropdrage.simpleweather.data.weather.test.local.cache.util.converter

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.weather.local.cache.util.CacheUnits
import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.WeatherUnitsDeconverter
import com.dropdrage.simpleweather.data.weather.remote.utils.ApiUnits
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.CELSIUS_TO_FAHRENHEIT_OFFSET
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.HPA_TO_MM_HG_MODIFIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_MPH_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.KM_H_TO_M_S_MULTIPLIER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_KM_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.METER_TO_MILE_DIVIDER
import com.dropdrage.simpleweather.data.weather.utils.WeatherUnitsConversionModifiers.MM_TO_INCH_DIVIDER
import com.dropdrage.test.util.mockLogW
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Suppress("UnnecessaryVariable")
internal class WeatherUnitsDeconverterTest {

    //region Temperature

    @Test
    fun `deconvertTemperatureIfApiDontSupport supported`() {
        every { CacheUnits.isTemperatureConversionNeeded } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertTemperatureIfApiDontSupport not supported and ApiUnit Fahrenheit`() = mockkObject(ApiUnits) {
        every { CacheUnits.isTemperatureConversionNeeded } returns false
        every { ApiUnits.TEMPERATURE } returns TemperatureUnit.FAHRENHEIT
        val value = 124f
        val valueConverted = (value - CELSIUS_TO_FAHRENHEIT_OFFSET) / CELSIUS_TO_FAHRENHEIT_MODIFIER

        val result = WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertTemperatureIfApiDontSupport not supported but ApiUnit API_SUPPORTED_UNIT and throws`() =
        mockkObject(ApiUnits) {
            every { CacheUnits.isTemperatureConversionNeeded } returns false
            every { ApiUnits.TEMPERATURE } returns ApiUnits.API_SUPPORTED_UNIT
            val value = 124f

            assertThrows<IllegalStateException> {
                WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(value)
            }
        }

    @Test
    fun `deconvertTemperatureIfApiDontSupport not supported and ApiUnit Celsius`() = mockLogW {
        mockkObject(ApiUnits) {
            every { CacheUnits.isTemperatureConversionNeeded } returns false
            every { ApiUnits.TEMPERATURE } returns TemperatureUnit.CELSIUS
            val value = 124f
            val valueConverted = value

            val result = WeatherUnitsDeconverter.deconvertTemperatureIfApiDontSupport(value)

            assertEquals(valueConverted, result)
        }
    }

    //endregion

    //region Pressure

    @Test
    fun `deconvertPressureIfApiDontSupport supported`() {
        every { CacheUnits.isPressureConversionNeeded } returns true
        val value = 124
        val valueConverted = value

        val result = WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertPressureIfApiDontSupport not supported and ApiUnit MM_HG`() = mockkObject(ApiUnits) {
        every { CacheUnits.isPressureConversionNeeded } returns false
        every { ApiUnits.PRESSURE } returns PressureUnit.MM_HG
        val value = 124
        val valueConverted = (value / HPA_TO_MM_HG_MODIFIER).toInt()

        val result = WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertPressureIfApiDontSupport not supported but ApiUnit API_SUPPORTED_UNIT and throws`() =
        mockkObject(ApiUnits) {
            every { CacheUnits.isPressureConversionNeeded } returns false
            every { ApiUnits.PRESSURE } returns ApiUnits.API_SUPPORTED_UNIT
            val value = 124

            assertThrows<IllegalStateException> {
                WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(value)
            }
        }

    @Test
    fun `deconvertPressureIfApiDontSupport not supported and ApiUnit H_PASCAL`() = mockLogW {
        mockkObject(ApiUnits) {
            every { CacheUnits.isPressureConversionNeeded } returns false
            every { ApiUnits.PRESSURE } returns PressureUnit.H_PASCAL
            val value = 124
            val valueConverted = value

            val result = WeatherUnitsDeconverter.deconvertPressureIfApiDontSupport(value)

            assertEquals(valueConverted, result)
        }
    }

    //endregion

    //region Wind Speed

    @Test
    fun `deconvertWindSpeedIfApiDontSupport supported`() {
        every { CacheUnits.isWindSpeedConversionNeeded } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertWindSpeedIfApiDontSupport not supported and ApiUnit MPH`() = mockkObject(ApiUnits) {
        every { CacheUnits.isWindSpeedConversionNeeded } returns false
        every { ApiUnits.WIND_SPEED } returns WindSpeedUnit.MPH
        val value = 124f
        val valueConverted = value * KM_H_TO_MPH_DIVIDER

        val result = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertWindSpeedIfApiDontSupport not supported and ApiUnit M_S`() = mockkObject(ApiUnits) {
        every { CacheUnits.isWindSpeedConversionNeeded } returns false
        every { ApiUnits.WIND_SPEED } returns WindSpeedUnit.M_S
        val value = 124f
        val valueConverted = value * KM_H_TO_M_S_DIVIDER

        val result = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertWindSpeedIfApiDontSupport not supported and ApiUnit KNOTS`() = mockkObject(ApiUnits) {
        every { CacheUnits.isWindSpeedConversionNeeded } returns false
        every { ApiUnits.WIND_SPEED } returns WindSpeedUnit.KNOTS
        val value = 124f
        val valueConverted = value / KM_H_TO_M_S_MULTIPLIER

        val result = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertWindSpeedIfApiDontSupport not supported but ApiUnit API_SUPPORTED_UNIT and throws`() =
        mockkObject(ApiUnits) {
            every { CacheUnits.isWindSpeedConversionNeeded } returns false
            every { ApiUnits.WIND_SPEED } returns ApiUnits.API_SUPPORTED_UNIT
            val value = 124f

            assertThrows<IllegalStateException> {
                WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)
            }
        }

    @Test
    fun `deconvertWindSpeedIfApiDontSupport not supported and ApiUnit KM_H`() = mockLogW {
        mockkObject(ApiUnits) {
            every { CacheUnits.isWindSpeedConversionNeeded } returns false
            every { ApiUnits.WIND_SPEED } returns WindSpeedUnit.KM_H
            val value = 124f
            val valueConverted = value

            val result = WeatherUnitsDeconverter.deconvertWindSpeedIfApiDontSupport(value)

            assertEquals(valueConverted, result)
        }
    }

    //endregion

    //region Visibility

    @Test
    fun `deconvertVisibilityIfApiDontSupport supported`() {
        every { CacheUnits.isVisibilityConversionNeeded } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertVisibilityIfApiDontSupport not supported and ApiUnit K_METER`() = mockkObject(ApiUnits) {
        every { CacheUnits.isVisibilityConversionNeeded } returns false
        every { ApiUnits.VISIBILITY } returns VisibilityUnit.K_METER
        val value = 124f
        val valueConverted = value * METER_TO_KM_DIVIDER

        val result = WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertVisibilityIfApiDontSupport not supported and ApiUnit MILE`() = mockkObject(ApiUnits) {
        every { CacheUnits.isVisibilityConversionNeeded } returns false
        every { ApiUnits.VISIBILITY } returns VisibilityUnit.MILE
        val value = 124f
        val valueConverted = value * METER_TO_MILE_DIVIDER

        val result = WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertVisibilityIfApiDontSupport not supported but ApiUnit API_SUPPORTED_UNIT and throws`() =
        mockkObject(ApiUnits) {
            every { CacheUnits.isVisibilityConversionNeeded } returns false
            every { ApiUnits.VISIBILITY } returns ApiUnits.API_SUPPORTED_UNIT
            val value = 124f

            assertThrows<IllegalStateException> {
                WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(value)
            }
        }

    @Test
    fun `deconvertVisibilityIfApiDontSupport not supported and ApiUnit METER`() = mockLogW {
        mockkObject(ApiUnits) {
            every { CacheUnits.isVisibilityConversionNeeded } returns false
            every { ApiUnits.VISIBILITY } returns VisibilityUnit.METER
            val value = 124f
            val valueConverted = value

            val result = WeatherUnitsDeconverter.deconvertVisibilityIfApiDontSupport(value)

            assertEquals(valueConverted, result)
        }
    }

    //endregion

    //region Precipitation

    @Test
    fun `deconvertPrecipitationIfApiDontSupport supported`() {
        every { CacheUnits.isPrecipitationConversionNeeded } returns true
        val value = 124f
        val valueConverted = value

        val result = WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertPrecipitationIfApiDontSupport not supported and ApiUnit INCH`() = mockkObject(ApiUnits) {
        every { CacheUnits.isPrecipitationConversionNeeded } returns false
        every { ApiUnits.PRECIPITATION } returns PrecipitationUnit.INCH
        val value = 124f
        val valueConverted = value * MM_TO_INCH_DIVIDER

        val result = WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(value)

        assertEquals(valueConverted, result)
    }

    @Test
    fun `deconvertPrecipitationIfApiDontSupport not supported but ApiUnit API_SUPPORTED_UNIT and throws`() =
        mockkObject(ApiUnits) {
            every { CacheUnits.isPrecipitationConversionNeeded } returns false
            every { ApiUnits.VISIBILITY } returns ApiUnits.API_SUPPORTED_UNIT
            val value = 124f

            assertThrows<IllegalStateException> {
                WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(value)
            }
        }

    @Test
    fun `deconvertPrecipitationIfApiDontSupport not supported and ApiUnit MM`() = mockLogW {
        mockkObject(ApiUnits) {
            every { CacheUnits.isPrecipitationConversionNeeded } returns false
            every { ApiUnits.PRECIPITATION } returns PrecipitationUnit.MM
            val value = 124f
            val valueConverted = value

            val result = WeatherUnitsDeconverter.deconvertPrecipitationIfApiDontSupport(value)

            assertEquals(valueConverted, result)
        }
    }

    //endregion


    private companion object {

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            mockkObject(CacheUnits)
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            unmockkObject(CacheUnits)
        }

    }

}
