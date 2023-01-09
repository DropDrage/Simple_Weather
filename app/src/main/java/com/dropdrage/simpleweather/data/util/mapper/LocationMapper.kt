package com.dropdrage.simpleweather.data.util.mapper

import android.location.Location
import com.dropdrage.simpleweather.domain.location.LocationResult

private typealias DomainLocation = com.dropdrage.simpleweather.domain.location.Location

fun Location.toDomainLocation(): DomainLocation = DomainLocation(latitude.toFloat(), longitude.toFloat())

fun Location?.toLocationResult() =
    if (this != null) LocationResult.Success(toDomainLocation())
    else LocationResult.NoLocation