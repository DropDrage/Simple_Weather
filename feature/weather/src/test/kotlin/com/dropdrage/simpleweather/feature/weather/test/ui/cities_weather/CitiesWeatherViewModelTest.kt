package com.dropdrage.simpleweather.feature.weather.test.ui.cities_weather

import app.cash.turbine.test
import com.dropdrage.common.test.util.ViewModelScopeExtension
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.weather.domain.use_case.UpdateAllCitiesWeatherUseCase
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesWeatherViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MockKExtension::class, ViewModelScopeExtension::class)
internal class CitiesWeatherViewModelTest {

    @MockK
    lateinit var cityRepository: CityRepository

    @MockK
    lateinit var updateAllCitiesWeather: UpdateAllCitiesWeatherUseCase


    @Nested
    inner class cities {

        @Test
        fun `empty flow`() = runTest {
            coEvery { cityRepository.orderedCities } returns emptyFlow()
            val viewModel = createViewModel()

            val initialItem = viewModel.cities.first()
            assertThat(initialItem).isEmpty()
            val firstItem = withTimeoutOrNull(1.seconds) { viewModel.cities.drop(1).first() }
            assertNull(firstItem)
        }

        @Test
        fun `filled flow`() = runTest {
            val expectedFirst = listOf<City>(mockk(), mockk(), mockk())
            val expectedSecond = listOf<City>(mockk(), mockk(), mockk())
            val orderedCities = MutableSharedFlow<List<City>>()
            coEvery { cityRepository.orderedCities } returns orderedCities
            val viewModel = createViewModel()

            viewModel.cities.test {
                orderedCities.emit(expectedFirst)
                orderedCities.emit(expectedSecond)
                val initialItem = awaitItem()
                val firstCities = awaitItem()
                val secondCities = awaitItem()

                cancel()

                assertThat(initialItem).isEmpty()
                assertThat(firstCities).containsExactlyElementsIn(expectedFirst)
                assertThat(secondCities).containsExactlyElementsIn(expectedSecond)
            }
        }

    }

    @Test
    fun `updateWeather success`() = runTest {
        coEvery { cityRepository.orderedCities } returns emptyFlow()
        coJustRun { updateAllCitiesWeather() }
        val viewModel = createViewModel()

        viewModel.updateWeather()

        coVerifyOnce { updateAllCitiesWeather() }
    }


    private fun createViewModel() = CitiesWeatherViewModel(cityRepository, updateAllCitiesWeather)

}
