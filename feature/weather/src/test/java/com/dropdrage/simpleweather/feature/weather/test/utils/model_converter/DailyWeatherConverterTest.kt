package com.dropdrage.simpleweather.feature.weather.test.utils.model_converter

import com.dropdrage.common.test.util.justArgToString
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.DateFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.DayNameFormat
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.feature.weather.util.createDayWeatherNoHours
import com.dropdrage.simpleweather.feature.weather.util.toViewDayWeather
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class DailyWeatherConverterTest {

    @MockK
    lateinit var dateFormatter: DateFormatter

    @MockK
    lateinit var dayFormatter: DayNameFormat

    @MockK
    lateinit var unitsFormatter: WeatherUnitsFormatter

    private lateinit var converter: DailyWeatherConverter


    @BeforeEach
    fun setUp() {
        converter = DailyWeatherConverter(dateFormatter, dayFormatter, unitsFormatter)
    }


    @Test
    fun `convertToView success`() {
        val nowDate = LocalDate.now()
        every { dayFormatter.formatFromStartDay(any(), eq(nowDate)) } answers {
            firstArg<LocalDate>().dayOfWeek.toString()
        }
        justArgToString { dateFormatter.formatDayMonth(any()) }
        justArgToString { unitsFormatter.formatTemperature(any()) }
        val dayWeather = createDayWeatherNoHours(0)
        val expectedDayWeather = toViewDayWeather(dayWeather)

        val viewDayWeather = converter.convertToView(dayWeather, nowDate)

        assertEquals(expectedDayWeather, viewDayWeather)
    }

}
