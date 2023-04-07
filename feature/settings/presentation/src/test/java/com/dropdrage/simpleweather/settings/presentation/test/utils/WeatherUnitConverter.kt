package com.dropdrage.simpleweather.settings.presentation.test.utils

import android.content.Context
import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.PressureUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.VisibilityUnit
import com.dropdrage.simpleweather.data.settings.WeatherUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewPrecipitationUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewPressureUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewVisibilityUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewWindSpeedUnit
import com.dropdrage.simpleweather.settings.presentation.util.matchEnumsAsArguments
import com.dropdrage.simpleweather.settings.presentation.utils.WeatherUnitConverter
import com.dropdrage.test.util.verifyOnce
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
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

        Assertions.assertEquals(expectedSetting, setting)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherUnitsWithView")
    fun `convertToViewSetting success`(weatherUnit: WeatherUnit, expectedSetting: AnySetting) {
        every { context.getString(any()) } answers { firstArg<Int>().toString() }
        every { context.getString(any(), any()) } answers { firstArg<Int>().toString() }
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
        Assertions.assertEquals(expectedViewSetting, viewSetting)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherUnits")
    fun `convertToValue success`(weatherUnit: AnySetting) {
        every { context.getString(any(), any()) } answers { firstArg<Int>().toString() }

        val value = converter.convertToValue(weatherUnit)

        verifyOnce { context.getString(any(), any()) }
        Assertions.assertEquals(weatherUnit.unitResId.toString(), value)
    }


    companion object {

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
