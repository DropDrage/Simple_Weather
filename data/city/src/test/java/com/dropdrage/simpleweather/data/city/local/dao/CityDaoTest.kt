package com.dropdrage.simpleweather.data.city.local.dao

import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.city.local.model.CityModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class CityDaoTest {

    @MockK
    lateinit var dao: FakeCityDao


    @Test
    fun `updateOrders empty list`() = runTest {
        val updatedCities = slot<List<CityModel>>()
        coJustRun { dao.updateAll(capture(updatedCities)) }
        coEvery { dao.updateOrders(any()) } answers { callOriginal() }
        val cities = listOf(
            CityModel(1, "City", 0, Country("Country", "CY"), Location(1f, 2f)),
            CityModel(2, "City1", 1, Country("Country2", "CY"), Location(1f, 2f)),
            CityModel(3, "City2", 2, Country("Country", "CY"), Location(1f, 24f)),
            CityModel(5, "City2", 3, Country("Country", "CY"), Location(1f, 24f)),
        )
        val citiesCopy = cities.map(CityModel::copy)

        dao.updateOrders(citiesCopy)

        assertThat(updatedCities.captured).containsExactlyElementsIn(cities)
    }

    @Test
    fun `updateOrders not empty ordered list`() = runTest {
        val updatedCities = slot<List<CityModel>>()
        coJustRun { dao.updateAll(capture(updatedCities)) }
        coEvery { dao.updateOrders(any()) } answers { callOriginal() }
        val cities = emptyList<CityModel>()

        dao.updateOrders(cities)

        assertThat(updatedCities.captured).isEmpty()
    }


    abstract class FakeCityDao : CityDao

}
