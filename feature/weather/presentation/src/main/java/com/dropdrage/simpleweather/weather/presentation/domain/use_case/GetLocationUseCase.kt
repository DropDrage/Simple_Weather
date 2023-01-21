package com.dropdrage.simpleweather.weather.presentation.domain.use_case

import com.dropdrage.simpleweather.data.location.LocationResult
import com.dropdrage.simpleweather.data.location.LocationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class GetLocationUseCase @Inject constructor(private val locationTracker: LocationTracker) {
    suspend operator fun invoke(): Flow<LocationResult> {
        val locationResult = locationTracker.getCurrentLocation()
        return if (locationResult is LocationResult.NoLocation) locationTracker.requestLocationUpdate()
        else flowOf(locationResult)
    }
}
