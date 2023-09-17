package com.dropdrage.simpleweather.feature.weather.test.ui.city.weather.city

import app.cash.turbine.test
import com.dropdrage.common.domain.Resource
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.createListIndexed
import com.dropdrage.common.test.util.mockLogD
import com.dropdrage.common.test.util.runTestViewModelScope
import com.dropdrage.common.test.util.runTestViewModelScopeAndTurbine
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.city.domain.Country
import com.dropdrage.simpleweather.feature.weather.domain.weather.Weather
import com.dropdrage.simpleweather.feature.weather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.city.city.CityWeatherViewModel
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.feature.weather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.feature.weather.util.HOURS_IN_DAY
import com.dropdrage.simpleweather.feature.weather.util.createDayWeather
import com.dropdrage.simpleweather.feature.weather.util.mockConverters
import com.dropdrage.simpleweather.feature.weather.util.toViewCurrentDayWeather
import com.dropdrage.simpleweather.feature.weather.util.toViewCurrentHourWeather
import com.dropdrage.simpleweather.feature.weather.util.toViewDayWeather
import com.dropdrage.simpleweather.feature.weather.util.toViewHourWeather
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
internal class CityWeatherViewModelTest {

    @MockK
    lateinit var currentHourWeatherConverter: CurrentHourWeatherConverter

    @MockK
    lateinit var currentDayWeatherConverter: CurrentDayWeatherConverter

    @MockK
    lateinit var hourWeatherConverter: HourWeatherConverter

    @MockK
    lateinit var dailyWeatherConverter: DailyWeatherConverter

    @MockK
    lateinit var cityRepository: CityRepository

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var viewModel: CityWeatherViewModel


    @BeforeEach
    fun setUp() {
        viewModel = CityWeatherViewModel(
            currentHourWeatherConverter,
            currentDayWeatherConverter,
            hourWeatherConverter,
            dailyWeatherConverter,
            cityRepository,
            weatherRepository
        )
    }


    @Nested
    inner class updateCityName {

        @Test
        fun `returns error`() = runTestViewModelScope {
            coEvery { cityRepository.getCityWithOrder(eq(viewModel.order)) } returns Resource.Error(IOException())
            val expectedCityTitle = ViewCityTitle(TextMessage.UnknownErrorMessage, TextMessage.UnknownErrorMessage)

            viewModel.cityTitle.test {
                viewModel.updateCityName()

                val newCityTitle = awaitItem()

                cancel()

                coVerifyOnce { cityRepository.getCityWithOrder(eq(viewModel.order)) }
                assertEquals(expectedCityTitle, newCityTitle)
            }
        }

        @Test
        fun `returns success`() = runTestViewModelScope {
            val city = City("Name1", mockk(), Country("Country", "CY"))
            coEvery { cityRepository.getCityWithOrder(eq(viewModel.order)) } returns Resource.Success(city)

            viewModel.cityTitle.test {
                viewModel.updateCityName()

                val newCityTitle = awaitItem()

                cancel()

                coVerifyOnce { cityRepository.getCityWithOrder(eq(viewModel.order)) }
                assertEquals(city.name, newCityTitle.city.getMessage(mockk()))
                assertEquals(city.country.code, newCityTitle.countryCode.getMessage(mockk()))
            }
        }

    }

    @Nested
    inner class loadWeather {

        @Test
        fun `returns error IOException without message then UnknownMessage`() = runTestViewModelScope {
            coEvery { cityRepository.getCityWithOrder(eq(viewModel.order)) } returns Resource.Error(IOException())

            viewModel.error.test {
                viewModel.loadWeather()
                val error = awaitItem()

                cancel()

                Assertions.assertSame(TextMessage.UnknownErrorMessage, error)
            }
        }


        @Test
        fun `returns error IOException with message`() = runTestViewModelScope {
            val message = "Error Message"
            coEvery {
                cityRepository.getCityWithOrder(eq(viewModel.order))
            } returns Resource.Error(IOException(message))

            viewModel.error.test {
                viewModel.loadWeather()
                val error = awaitItem()

                cancel()

                assertEquals(message, error.getMessage(mockk()))
            }
        }

        @Test
        fun `returns success`() = runTestViewModelScopeAndTurbine { backgroundScope ->
            mockLogD {
                mockConverters(
                    currentHourWeatherConverter,
                    currentDayWeatherConverter,
                    hourWeatherConverter,
                    dailyWeatherConverter
                )
                val daysCount = 3
                val weather = Weather(createListIndexed(daysCount) { createDayWeather(it.toLong()) })
                val city = City("Name1", mockk(), Country("Country", "CY"))
                coEvery { cityRepository.getCityWithOrder(eq(viewModel.order)) } returns Resource.Success(city)
                coEvery { weatherRepository.getWeatherFromNow(eq(city.location)) } returns Resource.Success(weather)
                val expectedCurrentDayWeather = toViewCurrentDayWeather(weather.currentDayWeather)
                val expectedCurrentHourWeather = toViewCurrentHourWeather(weather.currentHourWeather)
                val expectedDailyWeather = weather.dailyWeather.map(::toViewDayWeather)
                val expectedHourlyWeather = weather.hourlyWeather.map(::toViewHourWeather)
                val currentDayWeatherFlow = viewModel.currentDayWeather.testIn(backgroundScope)
                val currentHourWeatherFlow = viewModel.currentHourWeather.testIn(backgroundScope)
                val dailyWeatherFlow = viewModel.dailyWeather.testIn(backgroundScope)
                val hourlyWeatherFlow = viewModel.hourlyWeather.testIn(backgroundScope)

                viewModel.loadWeather()

                verify(exactly = daysCount) { dailyWeatherConverter.convertToView(any(), any()) }
                coVerify(atLeast = HOURS_IN_DAY, atMost = daysCount * HOURS_IN_DAY) { // flaks
                    hourWeatherConverter.convertToView(any(), any())
                }
                verifyOnce {
                    currentDayWeatherConverter.convertToView(any())
                    currentHourWeatherConverter.convertToView(any())
                }
                assertEquals(expectedCurrentDayWeather, currentDayWeatherFlow.awaitItem())
                assertEquals(expectedCurrentHourWeather, currentHourWeatherFlow.awaitItem())
                assertThat(dailyWeatherFlow.awaitItem()).containsExactlyElementsIn(expectedDailyWeather)
                assertThat(hourlyWeatherFlow.awaitItem()).containsExactlyElementsIn(expectedHourlyWeather)
            }
        }

    }

}
