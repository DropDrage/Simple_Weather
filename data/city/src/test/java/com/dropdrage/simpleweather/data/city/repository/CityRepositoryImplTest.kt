package com.dropdrage.simpleweather.data.city.repository

import com.dropdrage.common.domain.Resource
import com.dropdrage.common.test.util.assertInstanceOf
import com.dropdrage.common.test.util.coVerifyNever
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.runTestWithMockLogE
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.city.local.dao.CityDao
import com.dropdrage.simpleweather.data.city.local.model.CityModel
import com.dropdrage.simpleweather.data.source.local.app.util.mapper.toDomain
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.Country
import com.google.common.truth.Truth.assertThat
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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

private const val FIRST_ORDER = 0

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class CityRepositoryImplTest {

    @MockK
    lateinit var dao: CityDao
    private lateinit var repository: CityRepositoryImpl


    @BeforeEach
    fun setUp() {
        repository = CityRepositoryImpl(dao)
    }


    @Nested
    inner class orderedCities {

        @Test
        fun `empty list`() = runTest {
            val orderedCities = emptyList<CityModel>()
            every { dao.getAllOrderedList() } returns flowOf(orderedCities)

            val result = repository.orderedCities.first()

            verifyOnce { dao.getAllOrderedList() }
            assertThat(result).isEmpty()
        }

        @Test
        fun `not empty list`() = runTest {
            val orderedCities = listOf(
                CityModel(1, "City", 0, Country("Country", "CY"), Location(1f, 2f)),
                CityModel(2, "City1", 1, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(3, "City2", 2, Country("Country", "CY"), Location(1f, 24f)),
            )
            every { dao.getAllOrderedList() } returns flowOf(orderedCities)
            val orderedCitiesDomain = orderedCities.map(CityModel::toDomain)

            val result = repository.orderedCities.first()

            verifyOnce { dao.getAllOrderedList() }
            assertThat(result).containsExactlyElementsIn(orderedCitiesDomain)
        }

    }

    @Nested
    inner class addCity {

        @Test
        fun `in empty db`() = runTest {
            coEvery { dao.getLastOrder() } returns null
            val insertedCity = slot<CityModel>()
            coJustRun { dao.insert(capture(insertedCity)) }
            val city = City("City", Location(1f, 2f), Country("Country", "CY"))

            repository.addCity(city)

            coVerifyOnce {
                dao.getLastOrder()
                dao.insert(any())
            }
            assertEquals(city.name, insertedCity.captured.name)
            assertEquals(city.location, insertedCity.captured.location)
            assertEquals(city.country, insertedCity.captured.country)
            assertEquals(FIRST_ORDER, insertedCity.captured.order)
        }

        @Test
        fun `in not empty db`() = runTest {
            val lastOrder = 2
            coEvery { dao.getLastOrder() } returns lastOrder
            val insertedCity = slot<CityModel>()
            coJustRun { dao.insert(capture(insertedCity)) }
            val city = City("City", Location(1f, 2f), Country("Country", "CY"))

            repository.addCity(city)

            coVerifyOnce {
                dao.getLastOrder()
                dao.insert(any())
            }
            assertEquals(city.name, insertedCity.captured.name)
            assertEquals(city.location, insertedCity.captured.location)
            assertEquals(city.country, insertedCity.captured.country)
            assertEquals(lastOrder + 1, insertedCity.captured.order)
        }

    }

    @Nested
    inner class getCityWithOrder {

        @Test
        fun success() = runTest {
            val order = 1
            val cityModelWithOrder = CityModel(1, "Name", order, Country("Country", "CY"), Location(1f, 2f))
            coEvery { dao.getWithOrder(order) } returns cityModelWithOrder

            val result = repository.getCityWithOrder(order)

            coVerifyOnce { dao.getWithOrder(eq(order)) }
            assertInstanceOf<Resource.Success<City>>(result)
            val resultData = result.data
            assertEquals(cityModelWithOrder.name, resultData.name)
            assertEquals(cityModelWithOrder.location, resultData.location)
            assertEquals(cityModelWithOrder.country, resultData.country)
        }

        @Test
        fun `throws CancellationException`() = runTest {
            val order = 1
            coEvery { dao.getWithOrder(order) } throws CancellationException()

            assertThrows<CancellationException> { repository.getCityWithOrder(order) }

            coVerifyOnce { dao.getWithOrder(eq(order)) }
        }

        @Test
        fun `throws exception without message  and returns error without message`() =
            runTestWithMockLogE {
                val order = 1
                val exception = IOException()
                coEvery { dao.getWithOrder(order) } throws exception

                val result = repository.getCityWithOrder(order)

                coVerifyOnce { dao.getWithOrder(eq(order)) }
                assertInstanceOf<Resource.Error<City>>(result)
                assertNull(result.message)
                assertEquals(exception, result.exception)
            }

        @Test
        fun `throws exception without message  and returns error with message`() =
            runTestWithMockLogE {
                val order = 1
                val exceptionMessage = "Error message"
                val exception = IOException(exceptionMessage)
                coEvery { dao.getWithOrder(order) } throws exception

                val result = repository.getCityWithOrder(order)

                coVerifyOnce { dao.getWithOrder(eq(order)) }
                assertInstanceOf<Resource.Error<City>>(result)
                assertEquals(exceptionMessage, result.message)
                assertEquals(exception, result.exception)
            }

    }

    @Nested
    inner class getAllCitiesOrdered {

        @Test
        fun `empty list`() = runTest {
            val orderedCities = emptyList<CityModel>()
            coEvery { dao.getAllOrdered() } returns orderedCities

            val result = repository.getAllCitiesOrdered()

            coVerifyOnce { dao.getAllOrdered() }
            assertThat(result).isEmpty()
        }

        @Test
        fun `not empty list`() = runTest {
            val orderedCities = listOf(
                CityModel(1, "City", 0, Country("Country", "CY"), Location(1f, 2f)),
                CityModel(2, "City1", 1, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(3, "City2", 2, Country("Country", "CY"), Location(1f, 24f)),
            )
            coEvery { dao.getAllOrdered() } returns orderedCities
            val orderedCitiesDomain = orderedCities.map(CityModel::toDomain)

            val result = repository.getAllCitiesOrdered()

            coVerifyOnce { dao.getAllOrdered() }
            assertThat(result).containsExactlyElementsIn(orderedCitiesDomain)
        }

    }

    @Nested
    inner class updateCitiesOrders {

        @Test
        fun `empty list`() = runTest {
            val cityModels = emptyList<CityModel>()
            coEvery { dao.getAll() } returns cityModels
            val updatedCities = captureUpdate()
            val cities = emptyList<City>()

            repository.updateCitiesOrders(cities)

            coVerifyOnce {
                dao.getAll()
                dao.updateOrders(any())
            }
            assertThat(updatedCities.captured).isEmpty()
        }

        @Test
        fun `not empty list`() = runTest {
            val cityModels = listOf(
                CityModel(1, "City", 0, Country("Country", "CY"), Location(1f, 2f)),
                CityModel(2, "City1", 1, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(3, "City2", 2, Country("Country", "CY"), Location(1f, 24f)),
            )
            coEvery { dao.getAll() } returns cityModels
            val updatedCities = captureUpdate()
            val cities = cityModels.map(CityModel::toDomain)
            val cityModelsReordered = cityModels.mapIndexed { i, cityModel -> cityModel.copy(order = i) }

            repository.updateCitiesOrders(cities)

            coVerifyOnce {
                dao.getAll()
                dao.updateOrders(any())
            }
            assertThat(updatedCities.captured).containsExactlyElementsIn(cityModelsReordered)
        }

    }

    @Nested
    inner class deleteCity {

        @Test
        fun `list is empty`() = runTest {
            val cityModelToDelete = CityModel(3, "City2", 0, Country("Country", "CY"), Location(1f, 24f))
            coJustRun {
                dao.delete(
                    eq(cityModelToDelete.name),
                    eq(cityModelToDelete.country.code),
                    eq(cityModelToDelete.location.latitude),
                    eq(cityModelToDelete.location.longitude)
                )
            }
            val cityModels = emptyList<CityModel>()
            coEvery { dao.getAllOrdered() } returns cityModels
            val city = cityModelToDelete.toDomain()

            repository.deleteCity(city)

            coVerifyOnce {
                dao.delete(any(), any(), any(), any())
                dao.getAllOrdered()
            }
            coVerifyNever { dao.updateOrders(any()) }
        }

        @Test
        fun `from start of order`() = runTest {
            val cityModelToDelete = CityModel(3, "City2", 0, Country("Country", "CY"), Location(1f, 24f))
            coJustRun {
                dao.delete(
                    eq(cityModelToDelete.name),
                    eq(cityModelToDelete.country.code),
                    eq(cityModelToDelete.location.latitude),
                    eq(cityModelToDelete.location.longitude)
                )
            }
            val cityModels = listOf(
                CityModel(2, "City1", 1, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(1, "City", 2, Country("Country", "CY"), Location(1f, 2f)),
            )
            coEvery { dao.getAllOrdered() } returns cityModels
            val updatedCities = captureUpdate()
            val city = cityModelToDelete.toDomain()
            val cityModelsReordered = cityModels.filter { it != cityModelToDelete }
                .mapIndexed { i, cityModel -> cityModel.copy(order = i) }

            repository.deleteCity(city)

            coVerifyOnce {
                dao.delete(any(), any(), any(), any())
                dao.getAllOrdered()
                dao.updateOrders(any())
            }
            assertThat(updatedCities.captured).containsExactlyElementsIn(cityModelsReordered)
        }

        @Test
        fun `from middle of order`() = runTest {
            val cityModelToDelete = CityModel(3, "City2", 1, Country("Country", "CY"), Location(1f, 24f))
            coJustRun {
                dao.delete(
                    eq(cityModelToDelete.name),
                    eq(cityModelToDelete.country.code),
                    eq(cityModelToDelete.location.latitude),
                    eq(cityModelToDelete.location.longitude)
                )
            }
            val cityModels = listOf(
                CityModel(2, "City1", 0, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(1, "City", 2, Country("Country", "CY"), Location(1f, 2f)),
            )
            coEvery { dao.getAllOrdered() } returns cityModels
            val updatedCities = captureUpdate()
            val city = cityModelToDelete.toDomain()
            val cityModelsReordered = cityModels.filter { it != cityModelToDelete }
                .mapIndexed { i, cityModel -> cityModel.copy(order = i) }

            repository.deleteCity(city)

            coVerifyOnce {
                dao.delete(any(), any(), any(), any())
                dao.getAllOrdered()
                dao.updateOrders(any())
            }
            assertThat(updatedCities.captured).containsExactlyElementsIn(cityModelsReordered)
        }

        @Test
        fun `from end of order`() = runTest {
            val cityModelToDelete = CityModel(3, "City2", 2, Country("Country", "CY"), Location(1f, 24f))
            coJustRun {
                dao.delete(
                    eq(cityModelToDelete.name),
                    eq(cityModelToDelete.country.code),
                    eq(cityModelToDelete.location.latitude),
                    eq(cityModelToDelete.location.longitude)
                )
            }
            val cityModels = listOf(
                CityModel(2, "City1", 0, Country("Country2", "CY"), Location(1f, 2f)),
                CityModel(1, "City", 1, Country("Country", "CY"), Location(1f, 2f)),
            )
            coEvery { dao.getAllOrdered() } returns cityModels
            val updatedCities = captureUpdate()
            val city = cityModelToDelete.toDomain()
            val cityModelsReordered = cityModels.filter { it != cityModelToDelete }
                .mapIndexed { i, cityModel -> cityModel.copy(order = i) }

            repository.deleteCity(city)

            coVerifyOnce {
                dao.delete(any(), any(), any(), any())
                dao.getAllOrdered()
                dao.updateOrders(any())
            }
            assertThat(updatedCities.captured).containsExactlyElementsIn(cityModelsReordered)
        }

    }


    private fun captureUpdate(): CapturingSlot<List<CityModel>> {
        val slot = slot<List<CityModel>>()
        coJustRun { dao.updateOrders(capture(slot)) }
        return slot
    }

}
