package com.dropdrage.simpleweather.feature.weather.test.ui.cities_weather

import app.cash.turbine.test
import com.dropdrage.adapters.pool.PrefetchPlainViewPool
import com.dropdrage.common.test.util.justMock
import com.dropdrage.common.test.util.runTestViewModelScope
import com.dropdrage.common.test.util.verifyOnce
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
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
    fun `isDailyWeatherPoolInitialized not initialized returns false`() {
        assertFalse(viewModel.isDailyWeatherPoolInitialized)
    }

    @Test
    fun `setCityTitle success`() = runTestViewModelScope {
        val expectedTitle = ViewCityTitle(mockk(), mockk())

        viewModel.currentCityTitle.test {
            viewModel.setCityTitle(expectedTitle)
            val newTitle = awaitItem()

            cancel()

            Assertions.assertEquals(expectedTitle, newTitle)
        }
    }

    @Nested
    inner class createHourWeatherPoolIfNeeded {

        @Test
        fun `not initialized then created`() = mockkObject(PrefetchPlainViewPool) {
            justMock { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }

            assertFalse(viewModel.isHourlyWeatherPoolInitialized)

            viewModel.createHourWeatherPoolIfNeeded(mockk(), mockk())

            verifyOnce { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }
            assertNotNull(viewModel.hourlyWeatherRecyclerPool)
            assertTrue(viewModel.isHourlyWeatherPoolInitialized)
        }

        @Test
        fun `initialized then not created`() = mockkObject(PrefetchPlainViewPool) {
            val viewModel = spyk(viewModel) {
                every { isHourlyWeatherPoolInitialized } returns true
            }

            viewModel.createHourWeatherPoolIfNeeded(mockk(), mockk())

            clearAllMocks()
            assertThrows<UninitializedPropertyAccessException> { viewModel.hourlyWeatherRecyclerPool }
            assertFalse(viewModel.isHourlyWeatherPoolInitialized)
        }

    }

    @Nested
    inner class createDailyWeatherPoolIfNeeded {

        @Test
        fun `createDailyWeatherPoolIfNeeded not initialized then created`() = mockkObject(PrefetchPlainViewPool) {
            justMock { PrefetchPlainViewPool.createPrefetchedWithCoroutineSupplier(any(), any(), any(), any()) }

            assertFalse(viewModel.isDailyWeatherPoolInitialized)

            viewModel.createDailyWeatherPoolIfNeeded(mockk(), mockk())

            assertNotNull(viewModel.dailyWeatherRecyclerPool)
            assertTrue(viewModel.isDailyWeatherPoolInitialized)
        }

        @Test
        fun `createDailyWeatherPoolIfNeeded initialized then not created`() = mockkObject(PrefetchPlainViewPool) {
            val viewModel = spyk(viewModel) {
                every { isDailyWeatherPoolInitialized } returns true
            }

            viewModel.createDailyWeatherPoolIfNeeded(mockk(), mockk())

            clearAllMocks()
            assertThrows<UninitializedPropertyAccessException> { viewModel.dailyWeatherRecyclerPool }
            assertFalse(viewModel.isDailyWeatherPoolInitialized)
        }

    }

}
