package com.dropdrage.simpleweather.feature.weather.test.utils.format

import android.content.Context
import android.text.format.DateFormat
import com.chibatching.kotpref.Kotpref
import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import com.dropdrage.simpleweather.data.settings.TimeFormat
import com.dropdrage.simpleweather.feature.weather.R
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.TimeFormatter
import com.dropdrage.test.util.justMock
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class TimeFormatterTest {

    @MockK
    lateinit var context: Context

    private lateinit var formatter: TimeFormatter


    @BeforeEach
    fun setUp() {
        formatter = TimeFormatter(context)
    }


    @Nested
    inner class formatAsHourOrNow {

        @Test
        fun `returns now`() {
            val now = "Now"
            every { context.getString(eq(R.string.weather_hourly_now)) } returns now
            val localTime = LocalDateTime.now()

            val hourOrNow = formatter.formatAsHourOrNow(localTime, true)

            assertEquals(now, hourOrNow)
        }

        @ParameterizedTest
        @EnumSource(value = TimeFormat::class)
        fun `not now returns hour`(format: TimeFormat) {
            every { GeneralPreferences.timeFormat } returns format
            val localTime = LocalDateTime.now()

            val hourOrNow = formatter.formatAsHourOrNow(localTime, false)

            val expectedFormat = localTime.format(format.hourFormatter)
            assertEquals(expectedFormat, hourOrNow)
        }

    }

    @ParameterizedTest
    @EnumSource(value = TimeFormat::class)
    fun `formatTime not now success`(format: TimeFormat) {
        every { GeneralPreferences.timeFormat } returns format
        val localTime = LocalDateTime.now()

        val time = formatter.formatTime(localTime)

        val expectedFormat = localTime.format(format.timeFormatter)
        assertEquals(expectedFormat, time)
    }


    private companion object {

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            mockkStatic(DateFormat::class) {
                Kotpref.init(mockk { justMock({ applicationContext }, { justMock { applicationContext } }) })
                every { DateFormat.is24HourFormat(any()) } returns true
                every { DateFormat.getDateFormatOrder(any()) } returns charArrayOf('d', 'M')

                mockkObject(GeneralPreferences)
            }
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            unmockkObject(GeneralPreferences)
        }

    }

}
