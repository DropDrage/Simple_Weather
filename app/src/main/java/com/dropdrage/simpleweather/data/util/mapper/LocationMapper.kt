package com.dropdrage.simpleweather.data.util.mapper

import android.location.Location
import com.dropdrage.simpleweather.domain.location.LocationResult

private typealias DomainLocation = com.dropdrage.simpleweather.domain.location.Location

fun Location.toDomainLocation(): DomainLocation {
    return DomainLocation(latitude, longitude)
}

fun Location?.toLocationResult() =
    if (this != null) LocationResult.Success(toDomainLocation())
    else LocationResult.NoLocation