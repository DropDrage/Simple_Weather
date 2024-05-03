package com.dropdrage.simpleweather.feature.city.list.presentation.ui

import app.cash.turbine.test
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.createListIndexed
import com.dropdrage.common.test.util.runTestViewModelScope
import com.dropdrage.common.test.util.verifyNever
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.city.domain.Country
import com.dropdrage.simpleweather.feature.city.list.domain.CityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.domain.use_case.ObserveCitiesWithWeatherUseCase
import com.dropdrage.simpleweather.feature.city.list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.utils.CityCurrentWeatherConverter
import com.google.common.truth.Truth.assertThat
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CityListViewModelTest {

    @MockK
    lateinit var observeCitiesWithWeather: ObserveCitiesWithWeatherUseCase

    @MockK
    lateinit var cityCurrentWeatherConverter: CityCurrentWeatherConverter

    @MockK
    lateinit var cityRepository: CityRepository


    @Nested
    inner class citiesCurrentWeathers {

        @Test
        fun `no value then emits empty list`() = runTestViewModelScope {
            every { observeCitiesWithWeather() } returns emptyFlow()
            val viewModel = CityListViewModel(observeCitiesWithWeather, cityCurrentWeatherConverter, cityRepository)

            viewModel.citiesCurrentWeathers.test {
                assertThat(awaitItem()).isEmpty()
                verifyNever { cityCurrentWeatherConverter.convertToView(any()) }
            }
        }

        @Test
        fun `emits empty then filled list`() = runTestViewModelScope {
            every { cityCurrentWeatherConverter.convertToView(any()) } answers {
                val (city, weather) = firstArg<CityCurrentWeather>()
                ViewCityCurrentWeather(
                    city,
                    ViewCurrentWeather(
                        weather?.temperature.toString(),
                        ViewWeatherType.fromWeatherType(weather?.weatherType ?: WeatherType.ClearSky)
                    )
                )
            }
            val cityCurrentWeathers = listOf(
                CityCurrentWeather(
                    City("City1", Location(1f, 2f), Country("Country1", "CY")),
                    CurrentWeather(1f, WeatherType.Foggy)
                ),
                CityCurrentWeather(
                    City("City2", Location(1f, 2f), Country("Country1", "CY")),
                    CurrentWeather(-1f, WeatherType.ModerateRain)
                ),
                CityCurrentWeather(
                    City("City3", Location(1f, 2f), Country("Country3", "RY")),
                    CurrentWeather(10f, WeatherType.HeavyRain)
                ),
            )
            val cityCurrentWeatherFlow = MutableSharedFlow<List<CityCurrentWeather>>()
            every { observeCitiesWithWeather() } returns cityCurrentWeatherFlow
            val viewModel = CityListViewModel(observeCitiesWithWeather, cityCurrentWeatherConverter, cityRepository)

            viewModel.citiesCurrentWeathers.test {
                cityCurrentWeatherFlow.emit(cityCurrentWeathers)

                val firstItem = awaitItem()
                val secondItem = awaitItem()

                cancel()

                verify(exactly = cityCurrentWeathers.size) { cityCurrentWeatherConverter.convertToView(any()) }
                assertThat(firstItem).isEmpty()
                val viewCityCurrentWeathers = cityCurrentWeathers.map(cityCurrentWeatherConverter::convertToView)
                assertThat(secondItem).containsExactlyElementsIn(viewCityCurrentWeathers)
            }
        }

    }

    @Nested
    inner class saveOrder {

        @Test
        fun `empty list`() = runTestViewModelScope {
            coJustRun { cityRepository.updateCitiesOrders(eq(emptyList())) }
            val viewModel = createViewModelNotForTestCitiesCurrentWeathers()

            viewModel.saveOrder(emptyList())

            coVerifyOnce { cityRepository.updateCitiesOrders(eq(emptyList())) }
        }

        @Test
        fun `filled list`() = runTestViewModelScope {
            val cities = listOf(
                City("Name", Location(1f, 2f), Country("Name", "CY")),
                City("Name1", Location(2f, 2f), Country("Name", "RY")),
                City("Name2", Location(4f, 3f), Country("Name2", "CY")),
            )
            coJustRun { cityRepository.updateCitiesOrders(eq(cities)) }
            val orderedCities = createListIndexed(cities.size) {
                mockk<ViewCityCurrentWeather> { every { city } returns cities[it] }
            }
            val viewModel = createViewModelNotForTestCitiesCurrentWeathers()

            viewModel.saveOrder(orderedCities)

            coVerifyOnce { cityRepository.updateCitiesOrders(eq(cities)) }
        }

    }


    @Test
    fun `deleteCity success`() = runTestViewModelScope {
        val city = City("Name", Location(1f, 2f), Country("Name", "CY"))
        coJustRun { cityRepository.deleteCity(eq(city)) }
        val viewCityCurrentWeather = mockk<ViewCityCurrentWeather> {
            every { this@mockk.city } returns city
        }
        val viewModel = createViewModelNotForTestCitiesCurrentWeathers()

        viewModel.deleteCity(viewCityCurrentWeather)

        coVerifyOnce { cityRepository.deleteCity(eq(city)) }
    }


    private fun createViewModelNotForTestCitiesCurrentWeathers(): CityListViewModel {
        every { this@CityListViewModelTest.observeCitiesWithWeather() } returns emptyFlow()
        return CityListViewModel(observeCitiesWithWeather, cityCurrentWeatherConverter, cityRepository)
    }

}
