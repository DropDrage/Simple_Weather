package com.dropdrage.simpleweather.domain.search

import com.dropdrage.simpleweather.domain.location.Location

data class Cities(val cities: List<City>)

data class City(
    val name: String,
    val location: Location,
    val countryCode: String,
    val country: String,
)
