package com.dropdrage.simpleweather.domain.city

import com.dropdrage.simpleweather.domain.location.Location

data class City(
    val name: String,
    val location: Location,
    val country: Country,
)
