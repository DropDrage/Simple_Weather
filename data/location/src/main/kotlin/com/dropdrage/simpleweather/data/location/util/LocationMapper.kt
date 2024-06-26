package com.dropdrage.simpleweather.data.location.util

import android.location.Location
import com.dropdrage.simpleweather.feature.weather.domain.location.LocationResult
import com.dropdrage.simpleweather.core.domain.Location as DomainLocation

internal fun Location.toDomainLocation(): DomainLocation = DomainLocation(latitude.toFloat(), longitude.toFloat())

internal fun Location?.toLocationResult() =
    if (this != null) LocationResult.Success(toDomainLocation())
    else LocationResult.NoLocation
