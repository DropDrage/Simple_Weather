package com.dropdrage.simpleweather.weather.presentation.test.domain.use_case

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.weather.domain.location.LocationTracker
import com.dropdrage.simpleweather.weather.presentation.domain.use_case.GetLocationUseCase
import com.dropdrage.test.util.coVerifyNever
import com.dropdrage.test.util.coVerifyOnce
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
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


    @ParameterizedTest
    @MethodSource("provideLocationResults")
    fun `invoke NoLocation returns location update`(locationUpdateResult: LocationResult) = runTest {
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
    @MethodSource("provideLocationResultsWithoutNoLocation")
    fun `invoke not NoLocation success`(locationUpdateResult: LocationResult) = runTest {
        coEvery { locationTracker.getCurrentLocation() } returns locationUpdateResult

        val location = getLocation().first()

        coVerifyNever { locationTracker.requestLocationUpdate() }
        coVerifyOnce { locationTracker.getCurrentLocation() }
        assertEquals(locationUpdateResult, location)
    }


    private companion object {

        @JvmStatic
        fun provideLocationResults() = Stream.of(
            LocationResult.NoLocation,
            LocationResult.GpsDisabled,
            LocationResult.NoPermission("permission"),
            LocationResult.Success(Location(1f, 2f)),
        )

        @JvmStatic
        fun provideLocationResultsWithoutNoLocation() = Stream.of(
            LocationResult.GpsDisabled,
            LocationResult.NoPermission("permission"),
            LocationResult.Success(Location(1f, 2f)),
        )

    }

}