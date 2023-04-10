package com.dropdrage.simpleweather.city_list.presentation.presentation.utils

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.domain.CityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.core.presentation.utils.WeatherUnitsFormatter
import com.dropdrage.test.util.justArgToString
import com.dropdrage.test.util.verifyNever
import com.dropdrage.test.util.verifyOnce
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CityCurrentWeatherConverterTest {

    @MockK
    lateinit var unitsFormatter: WeatherUnitsFormatter

    private lateinit var converter: CityCurrentWeatherConverter


    @BeforeEach
    fun setUp() {
        converter = CityCurrentWeatherConverter(unitsFormatter)
    }


    @Test
    fun `convertToView weather not null`() {
        val weather = CurrentWeather(1f, WeatherType.Foggy)
        val cityCurrentWeather = CityCurrentWeather(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            weather,
        )
        justArgToString { unitsFormatter.formatTemperature(eq(weather.temperature)) }

        val viewCityCurrentWeather = converter.convertToView(cityCurrentWeather)

        verifyOnce { unitsFormatter.formatTemperature(eq(weather.temperature)) }
        val expectedViewCityCurrentWeather = ViewCityCurrentWeather(
            cityCurrentWeather.city,
            ViewCurrentWeather(weather.temperature.toString(), ViewWeatherType.fromWeatherType(weather.weatherType))
        )
        assertEquals(expectedViewCityCurrentWeather, viewCityCurrentWeather)
    }

    @Test
    fun `convertToView weather null`() {
        val noTemperature = "No temperature"
        every { unitsFormatter.noTemperature } returns noTemperature
        val cityCurrentWeather = CityCurrentWeather(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            null,
        )

        val viewCityCurrentWeather = converter.convertToView(cityCurrentWeather)

        verifyNever { unitsFormatter.formatTemperature(any()) }
        val expectedViewCityCurrentWeather = ViewCityCurrentWeather(
            cityCurrentWeather.city,
            ViewCurrentWeather(noTemperature, ViewWeatherType.ClearSky)
        )
        assertEquals(expectedViewCityCurrentWeather, viewCityCurrentWeather)
    }

}
