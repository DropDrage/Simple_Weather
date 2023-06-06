package com.dropdrage.simpleweather.data.city.repository

import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.city.remote.CitiesDto
import com.dropdrage.simpleweather.data.city.remote.CityDto
import com.dropdrage.simpleweather.data.city.remote.SearchApi
import com.dropdrage.test.util.coVerifyOnce
import com.dropdrage.test.util.runTestWithMockLogE
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class CitySearchRepositoryImplTest {

    @MockK
    lateinit var api: SearchApi
    private lateinit var repository: CitySearchRepositoryImpl


    @BeforeEach
    fun setUp() {
        repository = CitySearchRepositoryImpl(api)
    }


    @Nested
    inner class searchCities {

        @Test
        fun `returns empty list success`() = runTest {
            val query = "query"
            val citiesDto = CitiesDto()
            coEvery { api.searchCities(eq(query)) } returns citiesDto

            val result = repository.searchCities(query)

            coVerifyOnce { api.searchCities(eq(query)) }
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val resultData = (result as Resource.Success<List<City>>).data
            assertThat(resultData).isEmpty()
        }

        @Test
        fun `returns not empty list success`() = runTest {
            val query = "query"
            val cityDtos = listOf(
                CityDto("City", 1f, 2f, "CY", "Country"),
                CityDto("City2", 3f, 2f, "CY", "Country2"),
            )
            val citiesDto = CitiesDto(cityDtos)
            coEvery { api.searchCities(eq(query)) } returns citiesDto
            val cities = cityDtos.map {
                City(it.name, Location(it.latitude, it.longitude), Country(it.country, it.countryCode))
            }

            val result = repository.searchCities(query)

            coVerifyOnce { api.searchCities(eq(query)) }
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val resultData = (result as Resource.Success<List<City>>).data
            assertThat(resultData).containsExactlyElementsIn(cities)
        }

        @Test
        fun `throws CancellationException`() = runTest {
            val query = "query"
            coEvery { api.searchCities(eq(query)) } throws CancellationException()

            assertThrows<CancellationException> { repository.searchCities(query) }

            coVerifyOnce { api.searchCities(eq(query)) }
        }

        @Test
        fun `throws exception without message and returns error`() = runTestWithMockLogE {
            val query = "query"
            val exception = IOException()
            coEvery { api.searchCities(eq(query)) } throws exception

            val result = repository.searchCities(query)

            coVerifyOnce { api.searchCities(eq(query)) }
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            val resultError = result as Resource.Error<List<City>>
            assertNull(resultError.message)
            assertEquals(exception, resultError.exception)
        }

        @Test
        fun `throws exception with message and returns error`() = runTestWithMockLogE {
            val query = "query"
            val exceptionMessage = "Error message"
            val exception = IOException(exceptionMessage)
            coEvery { api.searchCities(eq(query)) } throws exception

            val result = repository.searchCities(query)

            coVerifyOnce { api.searchCities(eq(query)) }
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            val resultError = result as Resource.Error<List<City>>
            assertEquals(exceptionMessage, resultError.message)
            assertEquals(exception, resultError.exception)
        }

    }

}
