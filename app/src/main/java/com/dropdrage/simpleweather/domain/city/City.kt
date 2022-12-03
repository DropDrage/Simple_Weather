package com.dropdrage.simpleweather.domain.city.search

import com.dropdrage.simpleweather.domain.city.Country
import com.dropdrage.simpleweather.domain.location.Location

data class City(
    val name: String,
    val location: Location,
    val country: Country,
)
