package com.dropdrage.simpleweather.weather.presentation.test.utils.model_converter

import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.weather.presentation.util.createHourWeather
import com.dropdrage.simpleweather.weather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.toViewHourWeather
import com.dropdrage.test.util.justArgToString
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
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
            every { timeFormatter.formatAsHourOrNow(any(), eq(false)) } answers {
                firstArg<LocalDateTime>().toLocalTime().toString()
            }
            justArgToString { unitsFormatter.formatTemperature(any()) }
            justArgToString { unitsFormatter.formatPressure(any()) }
            justArgToString { unitsFormatter.formatWindSpeed(any()) }
            justArgToString { unitsFormatter.formatHumidity(any()) }
            justArgToString { unitsFormatter.formatVisibility(any()) }
            val hourWeather = createHourWeather(LocalDateTime.now())
            val expectedCurrentHourWeather = toViewHourWeather(hourWeather)

            val viewCurrentHourWeather = converter.convertToView(hourWeather, LocalDateTime.MIN)

            assertEquals(expectedCurrentHourWeather, viewCurrentHourWeather)
        }


        @Test
        fun `returns now success`() {
            every { timeFormatter.formatAsHourOrNow(any(), eq(true)) } answers {
                firstArg<LocalDateTime>().toLocalTime().toString()
            }
            justArgToString { unitsFormatter.formatTemperature(any()) }
            justArgToString { unitsFormatter.formatPressure(any()) }
            justArgToString { unitsFormatter.formatWindSpeed(any()) }
            justArgToString { unitsFormatter.formatHumidity(any()) }
            justArgToString { unitsFormatter.formatVisibility(any()) }
            val dateTimeNow = LocalDateTime.now()
            val hourWeather = createHourWeather(dateTimeNow)
            val expectedCurrentHourWeather = toViewHourWeather(hourWeather, true)

            val viewCurrentHourWeather = converter.convertToView(hourWeather, dateTimeNow)

            assertEquals(expectedCurrentHourWeather, viewCurrentHourWeather)
        }

    }

}
