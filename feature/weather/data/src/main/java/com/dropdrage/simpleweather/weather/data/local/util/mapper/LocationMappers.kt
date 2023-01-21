package com.dropdrage.simpleweather.weather.data.local.util.mapper

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.weather.data.local.cache.model.LocationModel

internal fun Location.toNewModel(): LocationModel =
    LocationModel(latitude = latitude, longitude = longitude)
