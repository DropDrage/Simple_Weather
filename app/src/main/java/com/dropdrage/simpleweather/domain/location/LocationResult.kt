package com.dropdrage.simpleweather.domain.location

sealed class LocationResult {
    class Success(val location: Location) : LocationResult()
    class NoPermission(val permission: String) : LocationResult(), LocationErrorResult
    object GpsDisabled : LocationResult(), LocationErrorResult
    object NoLocation : LocationResult(), LocationErrorResult
}

sealed interface LocationErrorResult
