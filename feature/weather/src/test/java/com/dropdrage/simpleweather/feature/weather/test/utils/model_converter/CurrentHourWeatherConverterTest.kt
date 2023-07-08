package com.dropdrage.simpleweather.feature.weather.test.utils.model_converter

import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.feature.weather.util.createHourWeather
import com.dropdrage.simpleweather.feature.weather.util.toViewCurrentHourWeather
import com.dropdrage.test.util.justArgToString
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class CurrentHourWeatherConverterTest {

    @MockK
    lateinit var unitsFormatter: WeatherUnitsFormatter

    private lateinit var converter: CurrentHourWeatherConverter


    @BeforeEach
    fun setUp() {
        converter = CurrentHourWeatherConverter(unitsFormatter)
    }


    @Test
    fun `convertToView success`() {
        justArgToString { unitsFormatter.formatTemperature(any()) }
        justArgToString { unitsFormatter.formatPressure(any()) }
        justArgToString { unitsFormatter.formatWindSpeed(any()) }
        justArgToString { unitsFormatter.formatHumidity(any()) }
        justArgToString { unitsFormatter.formatVisibility(any()) }
        val hourWeather = createHourWeather(LocalDateTime.now(), 10)
        val expectedCurrentHourWeather = toViewCurrentHourWeather(hourWeather)

        val viewCurrentHourWeather = converter.convertToView(hourWeather)

        assertEquals(expectedCurrentHourWeather, viewCurrentHourWeather)
    }

}
