package com.dropdrage.simpleweather.weather.presentation.test.utils.format

import com.chibatching.kotpref.Kotpref
import com.dropdrage.simpleweather.data.settings.DateFormat
import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import com.dropdrage.simpleweather.weather.presentation.util.format.DateFormatter
import com.dropdrage.test.util.justMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDate
import android.text.format.DateFormat as AndroidDateFormat

internal class DateFormatterTest {

    private val formatter = DateFormatter()


    @ParameterizedTest
    @EnumSource(value = DateFormat::class)
    fun `formatDayMonth success`(format: DateFormat) {
        every { GeneralPreferences.dateFormat } returns format
        val localDate = LocalDate.now()

        val dayMonth = formatter.formatDayMonth(localDate)

        val expectedFormat = localDate.format(format.formatter)
        assertEquals(expectedFormat, dayMonth)
    }


    private companion object {
        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            mockkStatic(AndroidDateFormat::class) {
                Kotpref.init(mockk { justMock({ applicationContext }, { justMock { applicationContext } }) })
                every { AndroidDateFormat.is24HourFormat(any()) } returns true
                every { AndroidDateFormat.getDateFormatOrder(any()) } returns charArrayOf('d', 'M')

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
