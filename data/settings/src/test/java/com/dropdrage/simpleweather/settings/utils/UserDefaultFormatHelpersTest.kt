package com.dropdrage.simpleweather.settings.utils

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.text.format.DateFormat
import com.dropdrage.common.build_config_checks.isSdkVersionGreaterOrEquals
import com.dropdrage.simpleweather.data.settings.util.isDateFormatStraight
import com.dropdrage.simpleweather.data.settings.util.isLocaleMetric
import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.setStaticFields
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.Locale

private const val LANGUAGE_COUNTRY_DELIMITER = '-'

internal class UserDefaultFormatHelpersTest {

    @Nested
    inner class `isLocaleMetric measurement` {

        @Test
        fun `not SI for SDK P`() = mockkStatic(LocaleData::class, ULocale::class) {
            setStaticFields<LocaleData.MeasurementSystem>(
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("UK"),
                    LocaleData.MeasurementSystem.UK,
                    mockk()
                ),
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("US"),
                    LocaleData.MeasurementSystem.UK,
                    mockk()
                ),
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("SI"),
                    LocaleData.MeasurementSystem.UK,
                    mockk()
                ),
            ) {
                mockSdkGreaterCheck(true) {
                    justMock { ULocale.getDefault() }
                    every { LocaleData.getMeasurementSystem(any()) } returns LocaleData.MeasurementSystem.UK

                    val result = isLocaleMetric()

                    verify { LocaleData.getMeasurementSystem(any()) }
                    assertFalse(result)
                }
            }
        }

        @Test
        fun `SI for SDK P`() = mockkStatic(LocaleData::class, ULocale::class) {
            setStaticFields<LocaleData.MeasurementSystem>(
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("UK"),
                    LocaleData.MeasurementSystem.UK,
                    mockk(),
                ),
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("US"),
                    LocaleData.MeasurementSystem.UK,
                    mockk(),
                ),
                Triple(
                    LocaleData.MeasurementSystem::class.java.getField("SI"),
                    LocaleData.MeasurementSystem.UK,
                    mockk(),
                ),
            ) {
                mockSdkGreaterCheck(true) {
                    justMock { ULocale.getDefault() }
                    every { LocaleData.getMeasurementSystem(any()) } returns LocaleData.MeasurementSystem.SI

                    val result = isLocaleMetric()

                    verify { LocaleData.getMeasurementSystem(any()) }
                    assertTrue(result)
                }
            }
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                "en${LANGUAGE_COUNTRY_DELIMITER}US",
                "en${LANGUAGE_COUNTRY_DELIMITER}LR",
                "en${LANGUAGE_COUNTRY_DELIMITER}MM"
            ]
        )
        fun `not SI for SDK O`(languageCountry: String) = mockSdkGreaterCheck(false) {
            val languageCode = languageCountry.substringBefore(LANGUAGE_COUNTRY_DELIMITER)
            val countryCode = languageCountry.substringAfter(LANGUAGE_COUNTRY_DELIMITER)
            setLocale(languageCode, countryCode) {
                val result = isLocaleMetric()

                assertFalse(result)
            }
        }

        @Test
        fun `SI for SDK O`() = mockSdkGreaterCheck(false) {
            setLocale(Locale.UK) {
                val result = isLocaleMetric()

                assertTrue(result)
            }
        }

    }

    @Nested
    inner class isDateFormatStraight {

        @Test
        fun `month first`() = mockkStatic(DateFormat::class) {
            every { DateFormat.getDateFormatOrder(any()) } returns charArrayOf('M', 'M', 'd', 'd', 'y', 'y', 'y', 'y')

            val result = isDateFormatStraight(mockk())

            assertFalse(result)
        }

        @Test
        fun `day first`() = mockkStatic(DateFormat::class) {
            every { DateFormat.getDateFormatOrder(any()) } returns charArrayOf('d', 'd', 'M', 'M', 'y', 'y', 'y', 'y')

            val result = isDateFormatStraight(mockk())

            assertTrue(result)
        }

    }


    private inline fun setLocale(languageCode: String, countryCode: String, block: () -> Unit) {
        setLocale(Locale(languageCode, countryCode), block)
    }

    private inline fun setLocale(locale: Locale, block: () -> Unit) {
        val defaultLocale = Locale.getDefault()
        Locale.setDefault(locale)
        block()
        Locale.setDefault(defaultLocale)
    }

    // ToDo temporary until Android testFixtures
    private inline fun mockSdkGreaterCheck(isGreater: Boolean, block: () -> Unit) =
        mockkStatic(::isSdkVersionGreaterOrEquals) {
            every { isSdkVersionGreaterOrEquals(any()) } returns isGreater
            block()
        }

}
