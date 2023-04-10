package com.dropdrage.simpleweather.weather.presentation.test.domain.use_case

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.weather.presentation.domain.use_case.UpdateAllCitiesWeatherUseCase
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.runTestWithMockLogE
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateAllCitiesWeatherUseCaseTest {

    @MockK
    lateinit var cityRepository: CityRepository

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var updateAllCitiesWeather: UpdateAllCitiesWeatherUseCase


    @BeforeEach
    fun setUp() {
        updateAllCitiesWeather = UpdateAllCitiesWeatherUseCase(cityRepository, weatherRepository)
    }


    @Test
    fun `invoke city empty list then nothing`() = runTest {
        coEvery { cityRepository.getAllCitiesOrdered() } returns emptyList()

        updateAllCitiesWeather()

        coVerifyNever { weatherRepository.updateWeather(any()) }
        coVerifyOnce { cityRepository.getAllCitiesOrdered() }
    }

    @Test
    fun `invoke city filled list then nothing`() = runTest {
        val allCitiesOrdered = listOf(
            City("Name", Location(1f, 2f), Country("Country", "CY")),
            City("Name2", Location(5f, 2f), Country("Country1", "CY")),
            City("Name3", Location(6f, 4f), Country("Country", "CY")),
        )
        coEvery { cityRepository.getAllCitiesOrdered() } returns allCitiesOrdered
        val updatedLocations = arrayListOf<Location>()
        coJustRun { weatherRepository.updateWeather(capture(updatedLocations)) }

        updateAllCitiesWeather()

        coVerifyOnce { cityRepository.getAllCitiesOrdered() }
        coVerify(exactly = allCitiesOrdered.size) { weatherRepository.updateWeather(any()) }
        assertThat(updatedLocations).containsExactlyElementsIn(allCitiesOrdered.map { it.location })
    }

    @Test
    fun `invoke throws but not throws it outside`() = runTestWithMockLogE {
        coEvery { cityRepository.getAllCitiesOrdered() } throws IOException()

        updateAllCitiesWeather()
    }

}
