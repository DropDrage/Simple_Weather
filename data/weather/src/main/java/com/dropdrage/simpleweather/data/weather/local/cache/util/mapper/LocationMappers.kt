package com.dropdrage.simpleweather.data.weather.local.cache.util.mapper

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel

internal fun Location.toNewModel(): LocationModel =
    LocationModel(latitude = latitude, longitude = longitude)
