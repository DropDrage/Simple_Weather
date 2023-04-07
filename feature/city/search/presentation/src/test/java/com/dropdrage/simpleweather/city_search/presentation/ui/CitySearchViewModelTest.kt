package com.dropdrage.simpleweather.city_search.presentation.ui

import app.cash.turbine.test
import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.city_search.domain.CitySearchRepository
import com.dropdrage.simpleweather.city_search.presentation.model.ViewCitySearchResult
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.mockLogE
import com.dropdrage.test.util.runTestViewModelScope
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CitySearchViewModelTest {

    @MockK
    lateinit var searchRepository: CitySearchRepository

    @MockK
    lateinit var cityRepository: CityRepository


    @Test
    fun `updateQuery debounce search error`() = runTestViewModelScope {
        mockLogE {
            val viewModel = createViewModel()
            val query = "query"
            coEvery { searchRepository.searchCities(query) } returns Resource.Error(IOException())

            viewModel.updateQuery(query)
            val searchResult = withTimeoutOrNull(1.seconds) { viewModel.searchResults.first() }

            coVerifyOnce { searchRepository.searchCities(query) }
            assertNull(searchResult)
        }
    }

    @Test
    fun `updateQuery debounce search success with empty list`() = runTestViewModelScope {
        val viewModel = createViewModel()
        val query = "query"
        coEvery { searchRepository.searchCities(query) } returns Resource.Success(emptyList())

        viewModel.updateQuery(query)
        viewModel.searchResults.test {
            val searchResults = awaitItem()
            cancelAndConsumeRemainingEvents()

            coVerifyOnce { searchRepository.searchCities(query) }
            assertThat(searchResults).isEmpty()
        }
    }

    @Test
    fun `updateQuery debounce search success with filled list`() = runTestViewModelScope {
        val viewModel = createViewModel()
        val query = "query"
        val cities = listOf(
            City("City1", Location(1f, 2f), Country("Country1", "CY")),
            City("City3", Location(2f, 2f), Country("Country2", "CY")),
            City("City2", Location(3f, 4f), Country("Country1", "RY")),
        )
        coEvery { searchRepository.searchCities(query) } returns Resource.Success(cities)

        viewModel.searchResults.test {
            viewModel.updateQuery(query)
            val searchResults = awaitItem()

            cancel()

            coVerifyOnce { searchRepository.searchCities(query) }
            assertThat(searchResults).containsExactlyElementsIn(cities.map(::ViewCitySearchResult))
        }
    }

    @Test
    fun `addCity success and emit cityAddedEvent`() = runTestViewModelScope {
        val cityResult = ViewCitySearchResult(City("City1", Location(1f, 2f), Country("Country1", "CY")))
        coJustRun { cityRepository.addCity(cityResult.city) }
        val viewModel = createViewModel()

        viewModel.cityAddedEvent.test {
            viewModel.addCity(cityResult)
            val cityAddedEvent = awaitItem()

            cancel()

            coVerifyOnce { cityRepository.addCity(cityResult.city) }
            assertSame(Unit, cityAddedEvent)
        }
    }


    private fun createViewModel() = CitySearchViewModel(searchRepository, cityRepository)

}
