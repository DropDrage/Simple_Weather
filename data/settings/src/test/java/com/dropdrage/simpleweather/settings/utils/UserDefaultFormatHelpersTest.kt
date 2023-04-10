package com.dropdrage.simpleweather.settings.utils

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import android.text.format.DateFormat
import com.dropdrage.simpleweather.settings.data.util.isDateFormatStraight
import com.dropdrage.simpleweather.settings.data.util.isLocaleMetric
import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.setSdk
import com.dropdrage.test.util.setStaticFields
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

private const val LANGUAGE_COUNTRY_DELIMITER = '-'

internal class UserDefaultFormatHelpersTest {

    @Test
    fun `isLocaleMetric measurement not SI for SDK P`() = setSdk(Build.VERSION_CODES.P) {
        mockkStatic(LocaleData::class, ULocale::class) {
            setStaticFields<LocaleData.MeasurementSystem>(
                arrayOf(
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
                )
            ) {
                justMock { ULocale.getDefault() }
                every { LocaleData.getMeasurementSystem(any()) } returns LocaleData.MeasurementSystem.UK

                val result = isLocaleMetric()

                verify { LocaleData.getMeasurementSystem(any()) }
                assertFalse(result)
            }
        }
    }

    @Test
    fun `isLocaleMetric measurement SI for SDK P`() = setSdk(Build.VERSION_CODES.P) {
        mockkStatic(LocaleData::class, ULocale::class) {
            setStaticFields<LocaleData.MeasurementSystem>(
                arrayOf(
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
                )
            ) {
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
    fun `isLocaleMetric measurement not SI for SDK O`(languageCountry: String) = setSdk(Build.VERSION_CODES.O) {
        val languageCode = languageCountry.substringBefore(LANGUAGE_COUNTRY_DELIMITER)
        val countryCode = languageCountry.substringAfter(LANGUAGE_COUNTRY_DELIMITER)
        setLocale(languageCode, countryCode) {
            val result = isLocaleMetric()

            assertFalse(result)
        }
    }

    @Test
    fun `isLocaleMetric measurement SI for SDK O`() = setSdk(Build.VERSION_CODES.O) {
        setLocale(Locale.UK) {
            val result = isLocaleMetric()

            assertTrue(result)
        }
    }

    @Test
    fun `isDateFormatStraight month first`() = mockkStatic(DateFormat::class) {
        every { DateFormat.getDateFormatOrder(any()) } returns charArrayOf('M', 'M', 'd', 'd', 'y', 'y', 'y', 'y')

        val result = isDateFormatStraight(mockk())

        assertFalse(result)
    }

    @Test
    fun `isDateFormatStraight day first`() = mockkStatic(DateFormat::class) {
        every { DateFormat.getDateFormatOrder(any()) } returns charArrayOf('d', 'd', 'M', 'M', 'y', 'y', 'y', 'y')

        val result = isDateFormatStraight(mockk())

        assertTrue(result)
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

}