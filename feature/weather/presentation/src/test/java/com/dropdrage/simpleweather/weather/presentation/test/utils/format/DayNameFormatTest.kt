package com.dropdrage.simpleweather.weather.presentation.test.utils.format

import android.content.Context
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.util.format.DayNameFormat
import com.dropdrage.test.util.justArgToString
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
internal class DayNameFormatTest {

    @MockK
    lateinit var context: Context

    private lateinit var format: DayNameFormat


    @BeforeEach
    fun setUp() {
        format = DayNameFormat(context)
    }


    @ParameterizedTest
    @MethodSource("provideOffsetWithRes")
    fun `formatFromStartDay success`(offset: Long, expectedRes: Int) {
        justArgToString { context.getString(expectedRes) }
        val day = LocalDate.ofEpochDay(offset)
        val startDay = LocalDate.ofEpochDay(0)

        val formatResult = format.formatFromStartDay(day, startDay)

        assertEquals(expectedRes.toString(), formatResult)
    }

    @ParameterizedTest
    @MethodSource("provideDayWeekWithRes")
    fun `formatFromStartDay with dayWeek`(dayOfWeek: DayOfWeek, expectedRes: Int) {
        justArgToString { context.getString(expectedRes) }
        val day = spyk(LocalDate.ofEpochDay(10)) {
            every { getDayOfWeek() } returns dayOfWeek
        }
        val startDay = LocalDate.ofEpochDay(0)

        val formatResult = format.formatFromStartDay(day, startDay)

        assertEquals(expectedRes.toString(), formatResult)
    }


    private companion object {
        @JvmStatic
        fun provideOffsetWithRes() = Stream.of(
            Arguments.of(0, R.string.day_today_short),
            Arguments.of(1, R.string.day_tomorrow_short),
        )

        @JvmStatic
        fun provideDayWeekWithRes() = Stream.of(
            Arguments.of(DayOfWeek.MONDAY, R.string.week_day_monday_short),
            Arguments.of(DayOfWeek.TUESDAY, R.string.week_day_tuesday_short),
            Arguments.of(DayOfWeek.WEDNESDAY, R.string.week_day_wednesday_short),
            Arguments.of(DayOfWeek.THURSDAY, R.string.week_day_thursday_short),
            Arguments.of(DayOfWeek.FRIDAY, R.string.week_day_friday_short),
            Arguments.of(DayOfWeek.SATURDAY, R.string.week_day_saturday_short),
            Arguments.of(DayOfWeek.SUNDAY, R.string.week_day_sunday_short),
        )
    }

}
