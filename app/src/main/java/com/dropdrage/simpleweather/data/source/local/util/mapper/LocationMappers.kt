package com.dropdrage.simpleweather.data.source.local.util.mapper

import com.dropdrage.simpleweather.data.source.local.cache.model.LocationModel
import com.dropdrage.simpleweather.domain.location.Location

fun Location.toNewModel(): LocationModel = LocationModel(latitude = latitude, longitude = longitude)
