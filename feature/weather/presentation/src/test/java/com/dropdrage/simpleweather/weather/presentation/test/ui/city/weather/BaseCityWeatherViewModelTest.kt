package com.dropdrage.simpleweather.weather.presentation.test.ui.city.weather

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.dropdrage.common.domain.CantObtainResourceException
import com.dropdrage.common.domain.Resource
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.simpleweather.weather.domain.weather.Weather
import com.dropdrage.simpleweather.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.BaseCityWeatherViewModel
import com.dropdrage.simpleweather.weather.presentation.util.HOURS_IN_DAY
import com.dropdrage.simpleweather.weather.presentation.util.createDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.mockConverters
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.HourWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.toViewCurrentDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.toViewCurrentHourWeather
import com.dropdrage.simpleweather.weather.presentation.util.toViewDayWeather
import com.dropdrage.simpleweather.weather.presentation.util.toViewHourWeather
import com.dropdrage.test.util.createListIndexed
import com.dropdrage.test.util.runTestViewModelScope
import com.dropdrage.test.util.verifyOnce
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BaseCityWeatherViewModelTest {

    @MockK
    lateinit var currentHourWeatherConverter: CurrentHourWeatherConverter

    @MockK
    lateinit var currentDayWeatherConverter: CurrentDayWeatherConverter

    @MockK
    lateinit var hourWeatherConverter: HourWeatherConverter

    @MockK
    lateinit var dailyWeatherConverter: DailyWeatherConverter

    private lateinit var viewModel: FakeBaseCityWeatherViewModel


    @BeforeEach
    fun setUp() {
        viewModel = FakeBaseCityWeatherViewModel(
            currentHourWeatherConverter,
            currentDayWeatherConverter,
            hourWeatherConverter,
            dailyWeatherConverter
        )
    }


    @Test
    fun `updateCityName success`() = runTestViewModelScope {
        val expectedCityTitle = mockk<ViewCityTitle>()
        viewModel.city = expectedCityTitle

        viewModel.cityTitle.test {
            viewModel.updateCityName()

            val newCityTitle = awaitItem()

            cancel()

            assertEquals(expectedCityTitle, newCityTitle)
        }
    }

    @Test
    fun `loadWeather isLoading false true false`() = runTestViewModelScope {
        viewModel.tryLoadWeatherFake = { delay(300L) }

        viewModel.isLoading.test {
            val beforeLoad = awaitItem()
            viewModel.loadWeather()
            val duringLoad = awaitItem()
            delay(300L)
            val afterLoad = awaitItem()

            cancel()

            assertFalse(beforeLoad)
            assertTrue(duringLoad)
            assertFalse(afterLoad)
        }
    }

    @Test
    fun `loadWeather error CantObtainResourceException`() = runTestViewModelScope {
        viewModel.weatherResult = Resource.Error(CantObtainResourceException())

        viewModel.error.test {
            viewModel.loadWeather()
            val error = awaitItem()

            cancel()

            assertSame(TextMessage.NoDataAvailableErrorMessage, error)
        }
    }

    @Test
    fun `loadWeather error IOException without message then UnknownMessage`() = runTestViewModelScope {
        viewModel.weatherResult = Resource.Error(IOException())

        viewModel.error.test {
            viewModel.loadWeather()
            val error = awaitItem()

            cancel()

            assertSame(TextMessage.UnknownErrorMessage, error)
        }
    }

    @Test
    fun `loadWeather error IOException with message`() = runTestViewModelScope {
        val message = "Error Message"
        viewModel.weatherResult = Resource.Error(IOException(message))

        viewModel.error.test {
            viewModel.loadWeather()
            val error = awaitItem()

            cancel()

            assertEquals(message, error.getMessage(mockk()))
        }
    }

    @Test
    fun `loadWeather success`() = runTestViewModelScope {
        mockConverters(
            currentHourWeatherConverter,
            currentDayWeatherConverter,
            hourWeatherConverter,
            dailyWeatherConverter
        )
        val daysCount = 3
        val weather = Weather(createListIndexed(daysCount) { createDayWeather(it.toLong()) })
        val expectedCurrentDayWeather = toViewCurrentDayWeather(weather.currentDayWeather)
        val expectedCurrentHourWeather = toViewCurrentHourWeather(weather.currentHourWeather)
        val expectedDailyWeather = weather.dailyWeather.map(::toViewDayWeather)
        val expectedHourlyWeather = weather.hourlyWeather.map(::toViewHourWeather)
        val currentDayWeatherFlow = viewModel.currentDayWeather.testIn(backgroundScope)
        val currentHourWeatherFlow = viewModel.currentHourWeather.testIn(backgroundScope)
        val dailyWeatherFlow = viewModel.dailyWeather.testIn(backgroundScope)
        val hourlyWeatherFlow = viewModel.hourlyWeather.testIn(backgroundScope)
        viewModel.weatherResult = Resource.Success(weather)

        viewModel.loadWeather()

        verify(exactly = daysCount) { dailyWeatherConverter.convertToView(any(), any()) }
        coVerify(atLeast = HOURS_IN_DAY, atMost = daysCount * HOURS_IN_DAY) { // flaks
            hourWeatherConverter.convertToView(any(), any())
        }
        verifyOnce {
            currentDayWeatherConverter.convertToView(any())
            currentHourWeatherConverter.convertToView(any())
        }
        assertEquals(expectedCurrentDayWeather, currentDayWeatherFlow.awaitItem())
        assertEquals(expectedCurrentHourWeather, currentHourWeatherFlow.awaitItem())
        assertThat(dailyWeatherFlow.awaitItem()).containsExactlyElementsIn(expectedDailyWeather)
        assertThat(hourlyWeatherFlow.awaitItem()).containsExactlyElementsIn(expectedHourlyWeather)
    }


    private class FakeBaseCityWeatherViewModel(
        currentHourWeatherConverter: CurrentHourWeatherConverter,
        currentDayWeatherConverter: CurrentDayWeatherConverter,
        hourWeatherConverter: HourWeatherConverter,
        dailyWeatherConverter: DailyWeatherConverter,
    ) : BaseCityWeatherViewModel(
        currentHourWeatherConverter,
        currentDayWeatherConverter,
        hourWeatherConverter,
        dailyWeatherConverter
    ) {

        var city: ViewCityTitle? = null

        lateinit var weatherResult: Resource<Weather>

        lateinit var tryLoadWeatherFake: suspend () -> Unit


        override suspend fun getCity(): ViewCityTitle = city ?: mockk()

        override suspend fun tryLoadWeather() {
            if (::tryLoadWeatherFake.isInitialized) {
                tryLoadWeatherFake()
            } else {
                processWeatherResult(weatherResult)
            }
        }

    }

}