package com.dropdrage.simpleweather.data.city.domain

import com.dropdrage.simpleweather.core.domain.Location

data class City(
    val name: String,
    val location: Location,
    val country: Country,
)
