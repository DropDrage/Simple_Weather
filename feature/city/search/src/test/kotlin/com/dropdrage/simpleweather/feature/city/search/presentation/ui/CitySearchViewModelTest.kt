package com.dropdrage.simpleweather.feature.city.search.presentation.ui

import app.cash.turbine.test
import com.dropdrage.common.domain.Resource
import com.dropdrage.common.test.util.ViewModelScopeExtension
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.CityRepository
import com.dropdrage.simpleweather.feature.city.domain.Country
import com.dropdrage.simpleweather.feature.city.search.domain.CitySearchRepository
import com.dropdrage.simpleweather.feature.city.search.presentation.model.ViewCitySearchResult
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MockKExtension::class, ViewModelScopeExtension::class)
internal class CitySearchViewModelTest {

    @MockK
    lateinit var searchRepository: CitySearchRepository

    @MockK
    lateinit var cityRepository: CityRepository


    @Nested
    inner class updateQuery {

        @Test
        fun `debounce search error`() = runTest {
            val viewModel = createViewModel()
            val query = "query"
            coEvery { searchRepository.searchCities(eq(query)) } returns Resource.Error(IOException())

            viewModel.updateQuery(query)
            val searchResult = withTimeoutOrNull(1.seconds) { viewModel.searchResults.first() }

            coVerifyOnce { searchRepository.searchCities(eq(query)) }
            Assertions.assertNull(searchResult)
        }

        @Test
        fun `debounce search success with empty list`() = runTest {
            val query = "query"
            coEvery { searchRepository.searchCities(eq(query)) } returns Resource.Success(emptyList())
            val viewModel = createViewModel()

            viewModel.searchResults.test {
                viewModel.updateQuery(query)

                val searchResults = awaitItem()

                cancel()

                coVerifyOnce { searchRepository.searchCities(eq(query)) }
                Truth.assertThat(searchResults).isEmpty()
            }
        }

        @Test
        fun `debounce search success with filled list`() = runTest {
            val viewModel = createViewModel()
            val query = "query"
            val cities = listOf(
                City("City1", Location(1f, 2f), Country("Country1", "CY")),
                City("City3", Location(2f, 2f), Country("Country2", "CY")),
                City("City2", Location(3f, 4f), Country("Country1", "RY")),
            )
            coEvery { searchRepository.searchCities(eq(query)) } returns Resource.Success(cities)

            viewModel.searchResults.test {
                viewModel.updateQuery(query)
                val searchResults = awaitItem()

                cancel()

                coVerifyOnce { searchRepository.searchCities(eq(query)) }
                Truth.assertThat(searchResults).containsExactlyElementsIn(cities.map(::ViewCitySearchResult))
            }
        }

    }

    @Test
    fun `addCity success and emit cityAddedEvent`() = runTest {
        val cityResult = ViewCitySearchResult(City("City1", Location(1f, 2f), Country("Country1", "CY")))
        coJustRun { cityRepository.addCity(eq(cityResult.city)) }
        val viewModel = createViewModel()

        viewModel.cityAddedEvent.test {
            viewModel.addCity(cityResult)
            val cityAddedEvent = awaitItem()

            cancel()

            coVerifyOnce { cityRepository.addCity(eq(cityResult.city)) }
            Assertions.assertSame(Unit, cityAddedEvent)
        }
    }


    private fun createViewModel() = CitySearchViewModel(searchRepository, cityRepository)

}