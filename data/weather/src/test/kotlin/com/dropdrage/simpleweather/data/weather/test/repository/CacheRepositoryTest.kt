package com.dropdrage.simpleweather.data.weather.test.repository

import com.dropdrage.common.test.util.coVerifyNever
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.common.test.util.createListIndexed
import com.dropdrage.common.test.util.runTestWithMockLogD
import com.dropdrage.simpleweather.data.weather.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.weather.repository.CacheRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CacheRepositoryTest {

    @MockK
    private lateinit var locationDao: LocationDao

    @MockK
    private lateinit var dayWeatherDao: DayWeatherDao

    private lateinit var cacheRepository: CacheRepository


    @BeforeEach
    fun setUp() {
        cacheRepository = CacheRepository(locationDao, dayWeatherDao)
    }


    @Nested
    inner class `clearOutdated locations` {

        @Test
        fun `ids empty then locations not deleted`() = runTestWithMockLogD {
            coEvery { dayWeatherDao.deleteOutdatedForAllLocations(LocalDate.now()) } returns 10
            coEvery { locationDao.getAllIds() } returns emptyList()

            cacheRepository.clearOutdated()

            coVerifyOnce { locationDao.getAllIds() }
            coVerifyNever {
                dayWeatherDao.hasWeatherForLocation(any())
                locationDao.delete(any<Long>())
            }
        }

        @Test
        fun `ids filled and all location have weathers then locations not deleted`() = runTestWithMockLogD {
            coEvery { dayWeatherDao.deleteOutdatedForAllLocations(LocalDate.now()) } returns 10
            val locations = createListIndexed(10) { it.toLong() }
            coEvery { locationDao.getAllIds() } returns locations
            coEvery { dayWeatherDao.hasWeatherForLocation(any()) } returns true

            cacheRepository.clearOutdated()

            coVerifyOnce { locationDao.getAllIds() }
            coVerify(exactly = locations.size) { dayWeatherDao.hasWeatherForLocation(any()) }
            coVerifyNever { locationDao.delete(any<Long>()) }
        }

        @Test
        fun `ids filled and all location have no weathers then locations deleted`() = runTestWithMockLogD {
            coEvery { dayWeatherDao.deleteOutdatedForAllLocations(LocalDate.now()) } returns 10
            val locations = createListIndexed(10) { it.toLong() }
            coEvery { locationDao.getAllIds() } returns locations
            coEvery { dayWeatherDao.hasWeatherForLocation(any()) } returns false
            coJustRun { locationDao.delete(any<Long>()) }

            cacheRepository.clearOutdated()

            coVerifyOnce { locationDao.getAllIds() }
            coVerify(exactly = locations.size) {
                dayWeatherDao.hasWeatherForLocation(any())
                locationDao.delete(any<Long>())
            }
        }

    }

    @Nested
    inner class hasCache {

        @Test
        fun `no items then false`() = runTest {
            coEvery { locationDao.hasItems() } returns false

            val hasCache = cacheRepository.hasCache()

            coVerifyOnce { locationDao.hasItems() }
            assertFalse(hasCache)
        }

        @Test
        fun `has items then true`() = runTest {
            coEvery { locationDao.hasItems() } returns true

            val hasCache = cacheRepository.hasCache()

            coVerifyOnce { locationDao.hasItems() }
            assertTrue(hasCache)
        }

    }

}
