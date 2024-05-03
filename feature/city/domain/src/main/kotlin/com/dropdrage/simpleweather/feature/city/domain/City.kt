package com.dropdrage.simpleweather.feature.city.domain

import com.dropdrage.simpleweather.core.domain.Location

data class City(
    val name: String,
    val location: Location,
    val country: Country,
)