package com.dropdrage.simpleweather.feature.settings.test.presentation.util

import android.content.Context
import com.dropdrage.common.test.util.justArgToString
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewPrecipitationUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewPressureUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewVisibilityUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewWindSpeedUnit
import com.dropdrage.simpleweather.feature.settings.presentation.util.WeatherUnitConverter
import com.dropdrage.simpleweather.feature.settings.util.matchEnumsAsArguments
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
internal class WeatherUnitConverterTest {

    @MockK
    lateinit var context: Context

    private lateinit var converter: WeatherUnitConverter


    @BeforeEach
    fun setUp() {
        converter = WeatherUnitConverter(context)
    }


    @ParameterizedTest
    @MethodSource("provideWeatherUnitsWithView")
    fun `convertToSetting success`(weatherUnit: WeatherUnit, expectedSetting: AnySetting) {
        val setting = converter.convertToSetting(weatherUnit)

        assertEquals(expectedSetting, setting)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherUnitsWithView")
    fun `convertToViewSetting success`(weatherUnit: WeatherUnit, expectedSetting: AnySetting) {
        justArgToString { context.getString(any()) }
        justArgToString { context.getString(any(), any()) }
        val expectedViewSetting = ViewSetting(
            label = expectedSetting.labelResId.toString(),
            currentValue = expectedSetting.unitResId.toString(),
            values = expectedSetting.values,
        )

        val viewSetting = converter.convertToViewSetting(weatherUnit)

        verifyOnce {
            context.getString(any())
            context.getString(any(), any())
        }
        assertEquals(expectedViewSetting, viewSetting)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherUnits")
    fun `convertToValue success`(weatherUnit: AnySetting) {
        justArgToString { context.getString(any(), any()) }

        val value = converter.convertToValue(weatherUnit)

        verifyOnce { context.getString(any(), any()) }
        assertEquals(weatherUnit.unitResId.toString(), value)
    }


    private companion object {

        @JvmStatic
        fun provideWeatherUnitsWithView() = Stream.of(
            *matchEnumsAsArguments(TemperatureUnit.values(), ViewTemperatureUnit.values()),
            *matchEnumsAsArguments(PressureUnit.values(), ViewPressureUnit.values()),
            *matchEnumsAsArguments(WindSpeedUnit.values(), ViewWindSpeedUnit.values()),
            *matchEnumsAsArguments(VisibilityUnit.values(), ViewVisibilityUnit.values()),
            *matchEnumsAsArguments(PrecipitationUnit.values(), ViewPrecipitationUnit.values()),
        )

        @JvmStatic
        fun provideWeatherUnits() = Stream.of(
            *ViewTemperatureUnit.values(),
            *ViewPressureUnit.values(),
            *ViewWindSpeedUnit.values(),
            *ViewVisibilityUnit.values(),
            *ViewPrecipitationUnit.values(),
        )

    }

}
