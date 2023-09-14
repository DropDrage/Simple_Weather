package com.dropdrage.simpleweather.feature.weather.domain.location

import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    suspend fun getCurrentLocation(): LocationResult

    suspend fun requestLocationUpdate(): Flow<LocationResult>

}
