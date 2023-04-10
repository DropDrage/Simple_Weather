package com.dropdrage.simpleweather.settings.presentation.test.ui

import app.cash.turbine.test
import com.chibatching.kotpref.Kotpref
import com.dropdrage.simpleweather.data.settings.*
import com.dropdrage.simpleweather.settings.presentation.SettingsViewModel
import com.dropdrage.simpleweather.settings.presentation.model.*
import com.dropdrage.simpleweather.settings.presentation.util.matchEnumsAsArguments
import com.dropdrage.simpleweather.settings.presentation.utils.GeneralFormatConverter
import com.dropdrage.simpleweather.settings.presentation.utils.WeatherUnitConverter
import com.dropdrage.test.util.justArgToString
import com.dropdrage.test.util.justCallOriginal
import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.runTestViewModelScope
import com.google.common.truth.Truth.assertThat
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass
import android.text.format.DateFormat as AndroidDateFormat

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsViewModelTest {

    @MockK
    lateinit var weatherUnitConverter: WeatherUnitConverter

    @MockK
    lateinit var generalFormatConverter: GeneralFormatConverter

    private lateinit var viewModel: SettingsViewModel


    @BeforeEach
    fun setUpEach() {
        mockPreferences()
        mockConversionsToViewSetting()
        viewModel = SettingsViewModel(weatherUnitConverter, generalFormatConverter)
    }


    @Test
    fun `settings converted correctly`() {
        val settings = viewModel.settings

        verify(exactly = 5) { weatherUnitConverter.convertToViewSetting(any()) }
        verify(exactly = 2) { generalFormatConverter.convertToViewSetting(any()) }
        val viewSettings = createListOfAllViewSettings()
        assertThat(settings).containsExactlyElementsIn(viewSettings)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherAnySettingsCombined")
    fun `getCurrentValue WeatherUnit success`(anySetting: AnySetting, expectedUnit: WeatherUnit) {
        mockWeatherPreference(expectedUnit)
        justCallOriginal { weatherUnitConverter.convertToSetting(any()) }

        val currentSetting = viewModel.getCurrentValue(anySetting)

        val expectedSetting = convertToSetting(expectedUnit)
        assertEquals(expectedSetting, currentSetting)
    }

    @ParameterizedTest
    @MethodSource("provideGeneralAnySettingsCombined")
    fun `getCurrentValue GeneralFormat success`(anySetting: AnySetting, expectedUnit: GeneralFormat) {
        mockGeneralPreference(expectedUnit)
        justCallOriginal { generalFormatConverter.convertToSetting(any()) }

        val currentSetting = viewModel.getCurrentValue(anySetting)

        val expectedSetting = convertToSetting(expectedUnit)
        assertEquals(expectedSetting, currentSetting)
    }

    @ParameterizedTest
    @MethodSource("provideWeatherAnySettingMatched")
    fun `changeSetting WeatherUnit success`(setting: AnySetting, expectedUnit: WeatherUnit) = runTestViewModelScope {
        justArgToString { weatherUnitConverter.convertToValue(any()) }
        val weatherUnitSlots = hashMapOf<KClass<out WeatherUnit>, CapturingSlot<out WeatherUnit>>()
        val newTemperatureUnitSlot = slot<TemperatureUnit>().also { weatherUnitSlots[TemperatureUnit::class] = it }
        justRun { WeatherUnitsPreferences.temperatureUnit = capture(newTemperatureUnitSlot) }
        val newPressureUnitSlot = slot<PressureUnit>().also { weatherUnitSlots[PressureUnit::class] = it }
        justRun { WeatherUnitsPreferences.pressureUnit = capture(newPressureUnitSlot) }
        val newWindSpeedUnitSlot = slot<WindSpeedUnit>().also { weatherUnitSlots[WindSpeedUnit::class] = it }
        justRun { WeatherUnitsPreferences.windSpeedUnit = capture(newWindSpeedUnitSlot) }
        val newVisibilityUnitSlot = slot<VisibilityUnit>().also { weatherUnitSlots[VisibilityUnit::class] = it }
        justRun { WeatherUnitsPreferences.visibilityUnit = capture(newVisibilityUnitSlot) }
        val newPrecipitationUnitSlot =
            slot<PrecipitationUnit>().also { weatherUnitSlots[PrecipitationUnit::class] = it }
        justRun { WeatherUnitsPreferences.precipitationUnit = capture(newPrecipitationUnitSlot) }

        viewModel.settingChanged.test {
            val expectedChangedSetting = viewModel.settings.first { it.values.contains(setting) }
            val expectedCurrentValue = setting.toString()

            viewModel.changeSetting(setting)

            val changedSetting = awaitItem()

            cancel()

            assertEquals(expectedChangedSetting, changedSetting)
            assertEquals(expectedCurrentValue, changedSetting.currentValue)
        }

        assertExpectedValueCapturedAndOthersNot(expectedUnit, weatherUnitSlots)
    }

    @ParameterizedTest
    @MethodSource("provideGeneralAnySettingMatched")
    fun `changeSetting GeneralFormat success`(setting: AnySetting, expectedFormat: GeneralFormat) =
        runTestViewModelScope {
            justArgToString { weatherUnitConverter.convertToValue(any()) }
            val weatherUnitSlots = hashMapOf<KClass<out GeneralFormat>, CapturingSlot<out GeneralFormat>>()
            val newTimeFormatSlot = slot<TimeFormat>().also { weatherUnitSlots[TimeFormat::class] = it }
            justRun { GeneralPreferences.timeFormat = capture(newTimeFormatSlot) }
            val newDateFormatSlot = slot<DateFormat>().also { weatherUnitSlots[DateFormat::class] = it }
            justRun { GeneralPreferences.dateFormat = capture(newDateFormatSlot) }

            viewModel.settingChanged.test {
                val expectedChangedSetting = viewModel.settings.first { it.values.contains(setting) }
                val expectedCurrentValue = setting.toString()

                viewModel.changeSetting(setting)

                val changedSetting = awaitItem()

                cancel()

                assertEquals(expectedChangedSetting, changedSetting)
                assertEquals(expectedCurrentValue, changedSetting.currentValue)
            }

            assertExpectedValueCapturedAndOthersNot(expectedFormat, weatherUnitSlots)
        }


    //region Utils

    private fun mockPreferences() {
        every { WeatherUnitsPreferences.temperatureUnit } returns TemperatureUnit.CELSIUS
        every { WeatherUnitsPreferences.pressureUnit } returns PressureUnit.MM_HG
        every { WeatherUnitsPreferences.windSpeedUnit } returns WindSpeedUnit.KM_H
        every { WeatherUnitsPreferences.visibilityUnit } returns VisibilityUnit.K_METER
        every { WeatherUnitsPreferences.precipitationUnit } returns PrecipitationUnit.MM
        every { GeneralPreferences.timeFormat } returns TimeFormat.H_24
        every { GeneralPreferences.dateFormat } returns DateFormat.STRAIGHT
    }

    private fun mockConversionsToViewSetting() {
        every { weatherUnitConverter.convertToViewSetting(any()) } answers {
            val unit = firstArg<WeatherUnit>()
            val viewUnit = convertToSetting(unit)
            ViewSetting(viewUnit.unitResId.toString(), viewUnit.unitResId.toString() + " Value", viewUnit.values)
        }
        every { generalFormatConverter.convertToViewSetting(any()) } answers {
            val unit = firstArg<GeneralFormat>()
            val viewUnit = convertToSetting(unit)
            ViewSetting(viewUnit.unitResId.toString(), viewUnit.unitResId.toString() + " Value", viewUnit.values)
        }
    }

    private fun createListOfAllViewSettings() = listOf(
        ViewSetting(
            ViewTemperatureUnit.CELSIUS.unitResId.toString(),
            ViewTemperatureUnit.CELSIUS.unitResId.toString() + " Value",
            ViewTemperatureUnit.CELSIUS.values
        ),
        ViewSetting(
            ViewPressureUnit.MM_HG.unitResId.toString(),
            ViewPressureUnit.MM_HG.unitResId.toString() + " Value",
            ViewPressureUnit.MM_HG.values
        ),
        ViewSetting(
            ViewWindSpeedUnit.KM_H.unitResId.toString(),
            ViewWindSpeedUnit.KM_H.unitResId.toString() + " Value",
            ViewWindSpeedUnit.KM_H.values
        ),
        ViewSetting(
            ViewVisibilityUnit.K_METER.unitResId.toString(),
            ViewVisibilityUnit.K_METER.unitResId.toString() + " Value",
            ViewVisibilityUnit.K_METER.values
        ),
        ViewSetting(
            ViewPrecipitationUnit.MM.unitResId.toString(),
            ViewPrecipitationUnit.MM.unitResId.toString() + " Value",
            ViewPrecipitationUnit.MM.values
        ),
        ViewSetting(
            ViewTimeFormat.H_24.unitResId.toString(),
            ViewTimeFormat.H_24.unitResId.toString() + " Value",
            ViewTimeFormat.H_24.values
        ),
        ViewSetting(
            ViewDateFormat.STRAIGHT.unitResId.toString(),
            ViewDateFormat.STRAIGHT.unitResId.toString() + " Value",
            ViewDateFormat.STRAIGHT.values
        )
    )

    private fun convertToSetting(unit: WeatherUnit): AnySetting = when (unit) {
        is DataTemperatureUnit -> ViewTemperatureUnit.fromData(unit)
        is DataPressureUnit -> ViewPressureUnit.fromData(unit)
        is DataWindSpeedUnit -> ViewWindSpeedUnit.fromData(unit)
        is DataVisibilityUnit -> ViewVisibilityUnit.fromData(unit)
        is DataPrecipitationUnit -> ViewPrecipitationUnit.fromData(unit)
    }

    private fun convertToSetting(format: GeneralFormat): AnySetting = when (format) {
        is TimeFormat -> ViewTimeFormat.fromData(format)
        is DateFormat -> ViewDateFormat.fromData(format)
    }

    private fun mockWeatherPreference(unit: WeatherUnit) = when (unit) {
        is TemperatureUnit -> every { WeatherUnitsPreferences.temperatureUnit } returns unit
        is PressureUnit -> every { WeatherUnitsPreferences.pressureUnit } returns unit
        is WindSpeedUnit -> every { WeatherUnitsPreferences.windSpeedUnit } returns unit
        is VisibilityUnit -> every { WeatherUnitsPreferences.visibilityUnit } returns unit
        is PrecipitationUnit -> every { WeatherUnitsPreferences.precipitationUnit } returns unit
        else -> throw IllegalArgumentException()
    }

    private fun mockGeneralPreference(format: GeneralFormat) = when (format) {
        is TimeFormat -> every { GeneralPreferences.timeFormat } returns format
        is DateFormat -> every { GeneralPreferences.dateFormat } returns format
        else -> throw IllegalArgumentException()
    }

    private fun <T : Any> assertExpectedValueCapturedAndOthersNot(
        expectedFormat: T,
        slots: Map<KClass<out T>, CapturingSlot<out T>>,
    ) {
        val expectedUnitClass = expectedFormat::class
        slots.forEach { entry ->
            if (entry.key != expectedUnitClass) {
                assertFalse(entry.value.isCaptured)
            } else {
                assertTrue(entry.value.isCaptured)
                assertEquals(expectedFormat, entry.value.captured)
            }
        }
    }

    //endregion


    private companion object {

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            Kotpref.init(mockk { justMock({ applicationContext }, { justMock { applicationContext } }) })
            mockkStatic(AndroidDateFormat::class) {
                every { AndroidDateFormat.is24HourFormat(any()) } returns true
                every { AndroidDateFormat.getDateFormatOrder(any()) } returns charArrayOf('d', 'M')

                mockkObject(WeatherUnitsPreferences, GeneralPreferences)
            }
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            unmockkObject(WeatherUnitsPreferences, GeneralPreferences)
        }


        @JvmStatic
        fun provideWeatherAnySettingsCombined() = Stream.of(
            *combineEnumsAsArguments(ViewTemperatureUnit.values(), TemperatureUnit.values()),
            *combineEnumsAsArguments(ViewPressureUnit.values(), PressureUnit.values()),
            *combineEnumsAsArguments(ViewWindSpeedUnit.values(), WindSpeedUnit.values()),
            *combineEnumsAsArguments(ViewVisibilityUnit.values(), VisibilityUnit.values()),
            *combineEnumsAsArguments(ViewPrecipitationUnit.values(), PrecipitationUnit.values()),
        )

        @JvmStatic
        fun provideGeneralAnySettingsCombined() = Stream.of(
            *combineEnumsAsArguments(ViewTimeFormat.values(), TimeFormat.values()),
            *combineEnumsAsArguments(ViewDateFormat.values(), DateFormat.values()),
        )

        private fun combineEnumsAsArguments(enums: Array<*>, combinedEnums: Array<*>): Array<Arguments> =
            enums.flatMap { view -> combinedEnums.map { unit -> Arguments.of(view, unit) } }.toTypedArray()

        @JvmStatic
        fun provideWeatherAnySettingMatched() = Stream.of(
            *matchEnumsAsArguments(ViewTemperatureUnit.values(), TemperatureUnit.values()),
            *matchEnumsAsArguments(ViewPressureUnit.values(), PressureUnit.values()),
            *matchEnumsAsArguments(ViewWindSpeedUnit.values(), WindSpeedUnit.values()),
            *matchEnumsAsArguments(ViewVisibilityUnit.values(), VisibilityUnit.values()),
            *matchEnumsAsArguments(ViewPrecipitationUnit.values(), PrecipitationUnit.values()),
        )

        @JvmStatic
        fun provideGeneralAnySettingMatched() = Stream.of(
            *matchEnumsAsArguments(ViewTimeFormat.values(), TimeFormat.values()),
            *matchEnumsAsArguments(ViewDateFormat.values(), DateFormat.values()),
        )

    }

}
