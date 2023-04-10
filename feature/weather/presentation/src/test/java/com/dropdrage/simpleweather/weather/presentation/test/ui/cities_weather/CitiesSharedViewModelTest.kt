package com.dropdrage.simpleweather.weather.presentation.test.ui.cities_weather

import app.cash.turbine.test
import com.dropdrage.adapters.pool.PrefetchPlainViewPool
import com.dropdrage.simpleweather.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.runTestViewModelScope
import com.dropdrage.test.util.verifyOnce
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
internal class CitiesSharedViewModelTest {

    private val viewModel = CitiesSharedViewModel()


    @Test
    fun `isHourlyWeatherPoolInitialized not initialized returns false`() {
        assertFalse(viewModel.isHourlyWeatherPoolInitialized)
    }

    @Test
    fun `createHourWeatherPoolIfNeeded not initialized then created`() =
        mockkObject(PrefetchPlainViewPool) {
            justMock { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }

            assertFalse(viewModel.isHourlyWeatherPoolInitialized)

            viewModel.createHourWeatherPoolIfNeeded(mockk(), mockk())

            verifyOnce { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }
            assertNotNull(viewModel.hourlyWeatherRecyclerPool)
            assertTrue(viewModel.isHourlyWeatherPoolInitialized)
        }

    @Test
    fun `createHourWeatherPoolIfNeeded initialized then not created`() =
        mockkObject(PrefetchPlainViewPool) {
            val viewModel = spyk(viewModel) {
                every { isHourlyWeatherPoolInitialized } returns true
            }

            viewModel.createHourWeatherPoolIfNeeded(mockk(), mockk())

            clearAllMocks()
            assertThrows<UninitializedPropertyAccessException> { viewModel.hourlyWeatherRecyclerPool }
            assertFalse(viewModel.isHourlyWeatherPoolInitialized)
        }


    @Test
    fun `isDailyWeatherPoolInitialized not initialized returns false`() {
        assertFalse(viewModel.isDailyWeatherPoolInitialized)
    }

    @Test
    fun `createDailyWeatherPoolIfNeeded not initialized then created`() =
        mockkObject(PrefetchPlainViewPool) {
            justMock { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }

            assertFalse(viewModel.isDailyWeatherPoolInitialized)

            viewModel.createDailyWeatherPoolIfNeeded(mockk(), mockk())

            assertNotNull(viewModel.dailyWeatherRecyclerPool)
            assertTrue(viewModel.isDailyWeatherPoolInitialized)
        }

    @Test
    fun `createDailyWeatherPoolIfNeeded initialized then not created`() =
        mockkObject(PrefetchPlainViewPool) {
            val viewModel = spyk(viewModel) {
                every { isDailyWeatherPoolInitialized } returns true
            }

            viewModel.createDailyWeatherPoolIfNeeded(mockk(), mockk())

            clearAllMocks()
            assertThrows<UninitializedPropertyAccessException> { viewModel.dailyWeatherRecyclerPool }
            assertFalse(viewModel.isDailyWeatherPoolInitialized)
        }


    @Test
    fun `setCityTitle success`() = runTestViewModelScope {
        val expectedTitle = ViewCityTitle(mockk(), mockk())

        viewModel.currentCityTitle.test {
            viewModel.setCityTitle(expectedTitle)
            val newTitle = awaitItem()

            cancel()

            assertEquals(expectedTitle, newTitle)
        }
    }

}