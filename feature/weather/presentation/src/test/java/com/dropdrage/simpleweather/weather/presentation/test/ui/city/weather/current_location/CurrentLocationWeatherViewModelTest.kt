package com.dropdrage.simpleweather.weather.presentation.test.ui.city.weather.current_location

import android.content.Context
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.dropdrage.common.domain.Resource
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.weather.domain.weather.Weather
import com.dropdrage.simpleweather.weather.domain.weather.WeatherRepository
import com.dropdrage.simpleweather.weather.presentation.R
import com.dropdrage.simpleweather.weather.presentation.domain.use_case.GetLocationUseCase
import com.dropdrage.simpleweather.weather.presentation.ui.city.weather.current_location.CurrentLocationWeatherViewModel
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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class CurrentLocationWeatherViewModelTest {

    @MockK
    lateinit var currentHourWeatherConverter: CurrentHourWeatherConverter

    @MockK
    lateinit var currentDayWeatherConverter: CurrentDayWeatherConverter

    @MockK
    lateinit var hourWeatherConverter: HourWeatherConverter

    @MockK
    lateinit var dailyWeatherConverter: DailyWeatherConverter

    @MockK
    lateinit var getLocation: GetLocationUseCase

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var viewModel: CurrentLocationWeatherViewModel


    @BeforeEach
    fun setUp() {
        viewModel = CurrentLocationWeatherViewModel(
            currentHourWeatherConverter,
            currentDayWeatherConverter,
            hourWeatherConverter,
            dailyWeatherConverter,
            getLocation,
            weatherRepository
        )
    }


    @Test
    fun `updateCityTitle success`() = runTestViewModelScope {
        viewModel.cityTitle.test {
            viewModel.updateCityName()
            val cityTitle = awaitItem()

            cancel()

            val locationTitle = "Location"
            val context = mockk<Context> {
                every { getString(eq(R.string.city_name_current_location)) } returns locationTitle
            }
            assertEquals(locationTitle, cityTitle.city.getMessage(context))
            assertSame(TextMessage.EmptyMessage, cityTitle.countryCode)
        }
    }

    @Test
    fun `loadWeather error NoLocation`() = runTestViewModelScope {
        coEvery { getLocation() } returns flowOf(LocationResult.NoLocation)

        viewModel.error.test {
            viewModel.loadWeather()
            val error = awaitItem()

            cancel()

            val message = "Error Message"
            val context = mockk<Context> {
                every { getString(any()) } returns message
            }
            assertEquals(message, error.getMessage(context))
        }
    }

    @Test
    fun `loadWeather error GpsDisabled`() = runTestViewModelScope {
        val locationResult = LocationResult.GpsDisabled
        coEvery { getLocation() } returns flowOf(locationResult)
        val locationErrorFlow = viewModel.locationObtainingError.testIn(backgroundScope)
        val errorFlow = viewModel.error.testIn(backgroundScope)

        viewModel.loadWeather()
        val locationError = locationErrorFlow.awaitItem()
        val error = errorFlow.awaitItem()

        assertSame(locationResult, locationError)
        val message = "Error Message"
        val context = mockk<Context> {
            every { getString(any()) } returns message
        }
        assertEquals(message, error.getMessage(context))
    }

    @Test
    fun `loadWeather error NoPermission`() = runTestViewModelScope {
        val locationResult = LocationResult.NoPermission("permission")
        coEvery { getLocation() } returns flowOf(locationResult)
        val locationErrorFlow = viewModel.locationObtainingError.testIn(backgroundScope)
        val errorFlow = viewModel.error.testIn(backgroundScope)

        viewModel.loadWeather()
        val locationError = locationErrorFlow.awaitItem()
        val error = errorFlow.awaitItem()

        assertSame(locationResult, locationError)
        val message = "Error Message"
        val context = mockk<Context> {
            every { getString(any()) } returns message
        }
        assertEquals(message, error.getMessage(context))
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
        val location = mockk<Location>()
        val locationResult = LocationResult.Success(location)
        coEvery { getLocation() } returns flowOf(locationResult)
        coEvery { weatherRepository.getUpdatedWeatherFromNow(eq(location)) } returns flowOf(Resource.Success(weather))
        val expectedCurrentDayWeather = toViewCurrentDayWeather(weather.currentDayWeather)
        val expectedCurrentHourWeather = toViewCurrentHourWeather(weather.currentHourWeather)
        val expectedDailyWeather = weather.dailyWeather.map(::toViewDayWeather)
        val expectedHourlyWeather = weather.hourlyWeather.map(::toViewHourWeather)
        val currentDayWeatherFlow = viewModel.currentDayWeather.testIn(backgroundScope)
        val currentHourWeatherFlow = viewModel.currentHourWeather.testIn(backgroundScope)
        val dailyWeatherFlow = viewModel.dailyWeather.testIn(backgroundScope)
        val hourlyWeatherFlow = viewModel.hourlyWeather.testIn(backgroundScope)

        viewModel.loadWeather()

        coVerify(exactly = daysCount) { dailyWeatherConverter.convertToView(any(), any()) }
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

}
