package com.dropdrage.simpleweather.feature.weather.test.domain.use_case

import com.dropdrage.common.test.util.coVerifyNever
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.runTestWithMockLogE
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.city.domain.Country
import com.dropdrage.simpleweather.feature.weather.domain.use_case.UpdateAllCitiesWeatherUseCase
import com.dropdrage.simpleweather.feature.weather.domain.weather.WeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
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


    @Nested
    inner class invoke {

        @Test
        fun `city empty list then nothing`() = runTest {
            coEvery { cityRepository.getAllCitiesOrdered() } returns emptyList()

            updateAllCitiesWeather()

            coVerifyNever { weatherRepository.updateWeather(any()) }
            coVerifyOnce { cityRepository.getAllCitiesOrdered() }
        }

        @Test
        fun `city filled list then nothing`() = runTest {
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
        fun `throws but not throws it outside`() = runTestWithMockLogE {
            coEvery { cityRepository.getAllCitiesOrdered() } throws IOException()

            updateAllCitiesWeather()
        }

    }

}
