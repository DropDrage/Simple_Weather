package com.dropdrage.simpleweather.data.util.mapper

import android.location.Location
import com.dropdrage.simpleweather.data.location.LocationResult

private typealias DomainLocation = com.dropdrage.simpleweather.core.domain.Location

internal fun Location.toDomainLocation(): DomainLocation = DomainLocation(latitude.toFloat(), longitude.toFloat())

internal fun Location?.toLocationResult() =
    if (this != null) LocationResult.Success(toDomainLocation())
    else LocationResult.NoLocation
