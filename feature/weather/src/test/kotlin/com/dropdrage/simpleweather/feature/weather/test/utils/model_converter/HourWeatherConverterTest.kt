package com.dropdrage.simpleweather.feature.weather.test.utils.model_converter

import com.dropdrage.common.test.util.createList
import com.dropdrage.common.test.util.createListIndexed
import com.dropdrage.common.test.util.justArgToString
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter.BulkWeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewHourWeather
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.TimeFormatter.BulkTimeFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.feature.weather.util.createHourWeather
import com.dropdrage.simpleweather.feature.weather.util.toViewHourWeather
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class HourWeatherConverterTest {

    @MockK
    lateinit var timeFormatter: TimeFormatter

    @MockK
    lateinit var unitsFormatter: WeatherUnitsFormatter

    private lateinit var converter: HourWeatherConverter


    @BeforeEach
    fun setUp() {
        converter = HourWeatherConverter(timeFormatter, unitsFormatter)
    }


    @Nested
    inner class convertToView {

        @Test
        fun `returns not now success`() {
            justArgToString { timeFormatter.formatAsHourOrNow(any(), eq(false)) }
            justArgToString { unitsFormatter.formatTemperature(any()) }
            justArgToString { unitsFormatter.formatPressure(any()) }
            justArgToString { unitsFormatter.formatWindSpeed(any()) }
            justArgToString { unitsFormatter.formatHumidity(any()) }
            justArgToString { unitsFormatter.formatVisibility(any()) }
            val hourWeather = createHourWeather(LocalDateTime.now())
            val expectedCurrentHourWeather = toViewHourWeather(hourWeather)

            val viewCurrentHourWeather = converter.convertToView(hourWeather, LocalDateTime.MIN)

            verifyOnce { timeFormatter.formatAsHourOrNow(any(), eq(false)) }
            verifyOnce { unitsFormatter.formatTemperature(any()) }
            verifyOnce { unitsFormatter.formatPressure(any()) }
            verifyOnce { unitsFormatter.formatWindSpeed(any()) }
            verifyOnce { unitsFormatter.formatHumidity(any()) }
            verifyOnce { unitsFormatter.formatVisibility(any()) }
            assertEquals(expectedCurrentHourWeather, viewCurrentHourWeather)
        }


        @Test
        fun `returns now success`() {
            justArgToString { timeFormatter.formatAsHourOrNow(any(), eq(true)) }
            justArgToString { unitsFormatter.formatTemperature(any()) }
            justArgToString { unitsFormatter.formatPressure(any()) }
            justArgToString { unitsFormatter.formatWindSpeed(any()) }
            justArgToString { unitsFormatter.formatHumidity(any()) }
            justArgToString { unitsFormatter.formatVisibility(any()) }
            val dateTimeNow = LocalDateTime.now()
            val hourWeather = createHourWeather(dateTimeNow)
            val expectedCurrentHourWeather = toViewHourWeather(hourWeather, true)

            val viewCurrentHourWeather = converter.convertToView(hourWeather, dateTimeNow)

            verifyOnce { timeFormatter.formatAsHourOrNow(any(), eq(true)) }
            verifyOnce { unitsFormatter.formatTemperature(any()) }
            verifyOnce { unitsFormatter.formatPressure(any()) }
            verifyOnce { unitsFormatter.formatWindSpeed(any()) }
            verifyOnce { unitsFormatter.formatHumidity(any()) }
            verifyOnce { unitsFormatter.formatVisibility(any()) }
            assertEquals(expectedCurrentHourWeather, viewCurrentHourWeather)
        }

    }


    @Nested
    inner class convertListToViewList {

        @Test
        fun `returns not now success`() {
            every { timeFormatter.bulkFormat<List<ViewHourWeather>>(any()) } answers {
                mockk<BulkTimeFormatter> {
                    justArgToString { formatAsHourOrNow(any(), eq(false)) }
                }.run(firstArg())
            }
            every { unitsFormatter.bulkFormat<List<ViewHourWeather>>(any()) } answers {
                mockk<BulkWeatherUnitsFormatter> {
                    justArgToString { formatTemperature(any()) }
                    justArgToString { formatPressure(any()) }
                    justArgToString { formatWindSpeed(any()) }
                    justArgToString { formatHumidity(any()) }
                    justArgToString { formatVisibility(any()) }
                }.run(firstArg())
            }
            val now = LocalDateTime.now()
            val hourWeather = createList(5) { createHourWeather(now) }
            val expectedCurrentHourWeather = hourWeather.map(::toViewHourWeather)

            val viewCurrentHourWeather = converter.convertToView(hourWeather, LocalDateTime.MIN)

            Truth.assertThat(viewCurrentHourWeather).containsExactlyElementsIn(expectedCurrentHourWeather)
        }

        @Test
        fun `returns now success`() {
            every { timeFormatter.bulkFormat<List<ViewHourWeather>>(any()) } answers {
                mockk<BulkTimeFormatter> {
                    justArgToString { formatAsHourOrNow(any(), any()) }
                }.run(firstArg())
            }
            every { unitsFormatter.bulkFormat<List<ViewHourWeather>>(any()) } answers {
                mockk<BulkWeatherUnitsFormatter> {
                    justArgToString { formatTemperature(any()) }
                    justArgToString { formatPressure(any()) }
                    justArgToString { formatWindSpeed(any()) }
                    justArgToString { formatHumidity(any()) }
                    justArgToString { formatVisibility(any()) }
                }.run(firstArg())
            }
            val now = LocalDateTime.now()
            val hourWeather = createListIndexed(5) {
                if (it == 0) createHourWeather(now)
                else createHourWeather(LocalDateTime.MIN)
            }
            val expectedCurrentHourWeather = hourWeather.map {
                toViewHourWeather(it, it.dateTime == now)
            }

            val viewCurrentHourWeather = converter.convertToView(hourWeather, now)

            Truth.assertThat(viewCurrentHourWeather).containsExactlyElementsIn(expectedCurrentHourWeather)
        }

    }

}
