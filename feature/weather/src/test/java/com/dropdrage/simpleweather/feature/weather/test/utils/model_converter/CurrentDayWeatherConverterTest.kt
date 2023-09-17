package com.dropdrage.simpleweather.feature.weather.test.utils.model_converter

import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.format.TimeFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.feature.weather.util.createDayWeatherNoHours
import com.dropdrage.simpleweather.feature.weather.util.toViewCurrentDayWeather
import com.dropdrage.test.util.justArgToString
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CurrentDayWeatherConverterTest {

    @MockK
    lateinit var timeFormatter: TimeFormatter

    @MockK
    lateinit var unitsFormatter: WeatherUnitsFormatter

    private lateinit var converter: CurrentDayWeatherConverter


    @BeforeEach
    fun setUp() {
        converter = CurrentDayWeatherConverter(timeFormatter, unitsFormatter)
    }


    @Test
    fun `convertToView success`() {
        justArgToString { timeFormatter.formatTime(any()) }
        justArgToString { unitsFormatter.formatTemperature(any()) }
        justArgToString { unitsFormatter.formatPrecipitation(any()) }
        justArgToString { unitsFormatter.formatWindSpeed(any()) }
        val dayWeather = createDayWeatherNoHours(0)
        val expectedCurrentDayWeather = toViewCurrentDayWeather(dayWeather)

        val viewCurrentDayWeather = converter.convertToView(dayWeather)

        assertEquals(expectedCurrentDayWeather, viewCurrentDayWeather)
    }

}
