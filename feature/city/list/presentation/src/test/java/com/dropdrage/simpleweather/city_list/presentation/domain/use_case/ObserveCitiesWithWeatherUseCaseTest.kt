package com.dropdrage.simpleweather.city_list.presentation.domain.use_case

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeatherRepository
import com.dropdrage.simpleweather.city_list.presentation.domain.CityCurrentWeather
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.runTestWithMockLogE
import com.dropdrage.test.util.verifyOnce
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
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

        var citiesWithWeather: List<CityCurrentWeather>? = emptyList()
        assertDoesNotThrow { citiesWithWeather = useCase().firstOrNull() }

        coVerifyNever { currentWeatherRepository.getCurrentWeather(any()) }
        verifyOnce { cityRepository.orderedCities }
        assertNull(citiesWithWeather)
    }

    @Test
    fun `invoke but orderedCities returns empty flow then null returned`() = runTest {
        coEvery { cityRepository.orderedCities } returns emptyFlow()

        var citiesWithWeather = useCase().firstOrNull()

        coVerifyNever { currentWeatherRepository.getCurrentWeather(any()) }
        verifyOnce { cityRepository.orderedCities }
        assertNull(citiesWithWeather)
    }

    @Test
    fun `invoke but getCurrentWeather throws then nothing returned`() = runTestWithMockLogE {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        coEvery { currentWeatherRepository.getCurrentWeather(any()).collect() } throws IOException()

        var citiesWithWeather: List<CityCurrentWeather>? = emptyList()
        assertDoesNotThrow { citiesWithWeather = useCase().firstOrNull() }

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        assertNull(citiesWithWeather)
    }

    @Test
    fun `invoke getCurrentWeather emits 1 item then 1 returned`() = runTest {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
        coEvery { cityRepository.orderedCities } returns flowOf(cities)
        val currentWeathers = listOf(
            CurrentWeather(1f, WeatherType.Foggy),
            CurrentWeather(10f, WeatherType.HeavyHailThunderstorm),
            CurrentWeather(-1f, WeatherType.DenseDrizzle),
        )
        coEvery { currentWeatherRepository.getCurrentWeather(any()) } returns flowOf(currentWeathers)

        var citiesWithWeather = useCase().toList()

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        val cityCurrentWeathers = cities.mapIndexed { i, city -> CityCurrentWeather(city, currentWeathers[i]) }
        assertThat(citiesWithWeather).hasSize(1)
        assertThat(citiesWithWeather[0]).containsExactlyElementsIn(cityCurrentWeathers)
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some old null item then 2 returned`() = runTest {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
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

        var citiesWithWeather = useCase().toList()

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        assertThat(citiesWithWeather).hasSize(2)
        val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, oldCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[0]).containsExactlyElementsIn(oldCityCurrentWeathers)
        val newCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, newCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[1]).containsExactlyElementsIn(newCityCurrentWeathers)
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some new null item then 2 returned`() = runTest {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
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

        var citiesWithWeather = useCase().toList()

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        assertThat(citiesWithWeather).hasSize(2)
        val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, oldCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[0]).containsExactlyElementsIn(oldCityCurrentWeathers)
        val newCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, newCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[1]).containsExactlyElementsIn(newCityCurrentWeathers)
    }

    @Test
    fun `invoke getCurrentWeather emits 2 but some of both null item then 2 returned`() = runTest {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
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

        var citiesWithWeather = useCase().toList()

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        assertThat(citiesWithWeather).hasSize(2)
        val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, oldCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[0]).containsExactlyElementsIn(oldCityCurrentWeathers)
        val newCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, newCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[1]).containsExactlyElementsIn(newCityCurrentWeathers)
    }

    @Test
    fun `invoke getCurrentWeather emits 2 item then 2 returned`() = runTest {
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
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

        var citiesWithWeather = useCase().toList()

        coVerifyOnce {
            cityRepository.orderedCities
            currentWeatherRepository.getCurrentWeather(any())
        }
        assertThat(citiesWithWeather).hasSize(2)
        val oldCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, oldCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[0]).containsExactlyElementsIn(oldCityCurrentWeathers)
        val newCityCurrentWeathers = cities.mapIndexed { i, city ->
            CityCurrentWeather(city, newCurrentWeathers[i])
        }
        assertThat(citiesWithWeather[1]).containsExactlyElementsIn(newCityCurrentWeathers)
    }

}