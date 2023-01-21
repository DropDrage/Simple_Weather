package com.dropdrage.simpleweather.data.location

import com.dropdrage.simpleweather.core.domain.Location

sealed class LocationResult {
    class Success(val location: Location) : LocationResult()
    class NoPermission(val permission: String) : LocationResult(), LocationErrorResult
    object GpsDisabled : LocationResult(), LocationErrorResult
    object NoLocation : LocationResult(), LocationErrorResult
}

sealed interface LocationErrorResult
