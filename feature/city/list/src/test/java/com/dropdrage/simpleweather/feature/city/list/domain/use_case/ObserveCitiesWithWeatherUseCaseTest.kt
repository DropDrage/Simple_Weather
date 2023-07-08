package com.dropdrage.simpleweather.feature.city.list.domain.use_case

import app.cash.turbine.test
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeatherRepository
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.feature.city.list.domain.CityCurrentWeather
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.runTestWithMockLogE
import com.dropdrage.test.util.verifyOnce
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
internal class ObserveCitiesWithWeatherUseCaseTest {

    @MockK
    private lateinit var cityRepository: CityRepository

    @MockK
    private lateinit var currentWeatherRepository: CurrentWeatherRepository

    private lateinit var useCase: ObserveCitiesWithWeatherUseCase


    @BeforeEach
    fun setUp() {
        useCase = ObserveCitiesWithWeatherUseCase(cityRepository, currentWeatherRepository)
    }


    @Test
    fun `invoke but orderedCities throws then nothing returned`() = runTestWithMockLogE {
        coEvery { cityRepository.orderedCities.collect() } throws IOException()

        useCase().test {
            awaitComplete()

            coVerifyNever { currentWeatherRepository.getCurrentWeather(any()) }
            verifyOnce { cityRepository.orderedCities }
        }
    }

    @Test
    fun `invoke but orderedCities returns empty flow then null returned`() = runTest {
        coEvery { cityRepository.orderedCities } returns emptyFlow()

        useCase().test {
            awaitComplete()

            coVerifyNever { currentWeatherRepository.getCurrentWeather(any()) }
            verifyOnce { cityRepository.orderedCities }
        }
    }

    @Test
    fun `invoke but getCurrentWeather throws then nothing returned`() = runTestWithMockLogE {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        coEvery { currentWeatherRepository.getCurrentWeather(any()).collect() } throws IOException()

        useCase().test {
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
        }
    }

    @Test
    fun `invoke getCurrentWeather emits 1 item then 1 returned`() = runTest {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val currentWeathers = listOf(
            CurrentWeather(1f, WeatherType.Foggy),
            CurrentWeather(10f, WeatherType.HeavyHailThunderstorm),
            CurrentWeather(-1f, WeatherType.DenseDrizzle),
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(currentWeathers)

        useCase().test {
            val citiesWithWeather = awaitItem()
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
            val cityCurrentWeathers = cities.mapIndexed { i, city -> CityCurrentWeather(city, currentWeathers[i]) }
            assertThat(citiesWithWeather).containsExactlyElementsIn(cityCurrentWeathers)
        }
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some old null item then 2 returned`() = runTest {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val oldCurrentWeathers = listOf(
            CurrentWeather(1f, WeatherType.Foggy),
            null,
            null,
        )
        val newCurrentWeathers = listOf(
            CurrentWeather(0f, WeatherType.DenseDrizzle),
            CurrentWeather(6f, WeatherType.LightDrizzle),
            CurrentWeather(-10f, WeatherType.ModerateRain),
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(
            oldCurrentWeathers, newCurrentWeathers
        )

        useCase().test {
            val firstCitiesWithWeather = awaitItem()
            val secondCitiesWithWeather = awaitItem()
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
            val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, oldCurrentWeathers[i])
            }
            assertThat(firstCitiesWithWeather).containsExactlyElementsIn(oldCityCurrentWeathers)
            val newCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, newCurrentWeathers[i])
            }
            assertThat(secondCitiesWithWeather).containsExactlyElementsIn(newCityCurrentWeathers)
        }
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some new null item then 2 returned`() = runTest {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val oldCurrentWeathers = listOf(
            CurrentWeather(1f, WeatherType.Foggy),
            CurrentWeather(10f, WeatherType.HeavyHailThunderstorm),
            CurrentWeather(-1f, WeatherType.DenseDrizzle),
        )
        val newCurrentWeathers = listOf(
            CurrentWeather(0f, WeatherType.DenseDrizzle),
            null,
            null,
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(
            oldCurrentWeathers, newCurrentWeathers
        )

        useCase().test {
            val firstCitiesWithWeather = awaitItem()
            val secondCitiesWithWeather = awaitItem()
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
            val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, oldCurrentWeathers[i])
            }
            assertThat(firstCitiesWithWeather).containsExactlyElementsIn(oldCityCurrentWeathers)
            val newCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, newCurrentWeathers[i])
            }
            assertThat(secondCitiesWithWeather).containsExactlyElementsIn(newCityCurrentWeathers)
        }
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some of both null item then 2 returned`() = runTest {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val oldCurrentWeathers = listOf(
            null,
            null,
            CurrentWeather(-1f, WeatherType.DenseDrizzle),
        )
        val newCurrentWeathers = listOf(
            CurrentWeather(0f, WeatherType.DenseDrizzle),
            null,
            null,
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(
            oldCurrentWeathers, newCurrentWeathers
        )

        useCase().test {
            val firstCitiesWithWeather = awaitItem()
            val secondCitiesWithWeather = awaitItem()
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
            val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, oldCurrentWeathers[i])
            }
            assertThat(firstCitiesWithWeather).containsExactlyElementsIn(oldCityCurrentWeathers)
            val newCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, newCurrentWeathers[i])
            }
            assertThat(secondCitiesWithWeather).containsExactlyElementsIn(newCityCurrentWeathers)
        }
    }

    @Test
    fun `invoke getCurrentWeather emits 2 item then 2 returned`() = runTest {
        val cities = CITIES
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val oldCurrentWeathers = listOf(
            CurrentWeather(1f, WeatherType.Foggy),
            CurrentWeather(10f, WeatherType.HeavyHailThunderstorm),
            CurrentWeather(-1f, WeatherType.DenseDrizzle),
        )
        val newCurrentWeathers = listOf(
            CurrentWeather(0f, WeatherType.DenseDrizzle),
            CurrentWeather(6f, WeatherType.LightDrizzle),
            CurrentWeather(-10f, WeatherType.ModerateRain),
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(
            oldCurrentWeathers, newCurrentWeathers
        )

        useCase().test {
            val firstCitiesWithWeather = awaitItem()
            val secondCitiesWithWeather = awaitItem()
            awaitComplete()

            coVerifyOnce {
                cityRepository.orderedCities
                currentWeatherRepository.getCurrentWeather(any())
            }
            val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, oldCurrentWeathers[i])
            }
            assertThat(firstCitiesWithWeather).containsExactlyElementsIn(oldCityCurrentWeathers)
            val newCityCurrentWeathers = cities.mapIndexed { i, city ->
                CityCurrentWeather(city, newCurrentWeathers[i])
            }
            assertThat(secondCitiesWithWeather).containsExactlyElementsIn(newCityCurrentWeathers)
        }
    }


    private companion object {
        private val CITIES = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
    }

}
