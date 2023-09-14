package com.dropdrage.simpleweather.feature.weather.test.domain.use_case

import com.dropdrage.common.test.util.coVerifyNever
import com.dropdrage.common.test.util.coVerifyOnce
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.feature.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.feature.weather.domain.location.LocationTracker
import com.dropdrage.simpleweather.feature.weather.domain.use_case.GetLocationUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class GetLocationUseCaseTest {

    @MockK
    lateinit var locationTracker: LocationTracker

    private lateinit var getLocation: GetLocationUseCase


    @BeforeEach
    fun setUp() {
        getLocation = GetLocationUseCase(locationTracker)
    }


    @Nested
    inner class invoke {

        @ParameterizedTest
        @ArgumentsSource(LocationResultsProvider::class)
        fun `NoLocation returns location update`(locationUpdateResult: LocationResult) = runTest {
            coEvery { locationTracker.getCurrentLocation() } returns LocationResult.NoLocation
            coEvery { locationTracker.requestLocationUpdate() } returns flowOf(locationUpdateResult)

            val location = getLocation().first()

            coVerifyOnce {
                locationTracker.getCurrentLocation()
                locationTracker.requestLocationUpdate()
            }
            assertEquals(locationUpdateResult, location)
        }

        @ParameterizedTest
        @ArgumentsSource(LocationResultsWithoutNoLocationProvider::class)
        fun `not NoLocation success`(locationUpdateResult: LocationResult) = runTest {
            coEvery { locationTracker.getCurrentLocation() } returns locationUpdateResult

            val location = getLocation().first()

            coVerifyNever { locationTracker.requestLocationUpdate() }
            coVerifyOnce { locationTracker.getCurrentLocation() }
            assertEquals(locationUpdateResult, location)
        }

    }


    private companion object {

        private object LocationResultsWithoutNoLocationProvider : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                Arguments.of(LocationResult.GpsDisabled),
                Arguments.of(LocationResult.NoPermission("permission")),
                Arguments.of(LocationResult.Success(Location(1f, 2f))),
            )
        }

        private object LocationResultsProvider : ArgumentsProvider {
            override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
                Arguments.of(LocationResult.NoLocation),
                Arguments.of(LocationResult.GpsDisabled),
                Arguments.of(LocationResult.NoPermission("permission")),
                Arguments.of(LocationResult.Success(Location(1f, 2f))),
            )
        }

    }

}
