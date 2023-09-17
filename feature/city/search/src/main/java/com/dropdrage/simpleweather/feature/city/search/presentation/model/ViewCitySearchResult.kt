package com.dropdrage.simpleweather.feature.city.search.presentation.model

import com.dropdrage.simpleweather.feature.city.domain.City

internal data class ViewCitySearchResult(val city: City) {
    val name: String = city.name
    val countryName: String = city.country.name
}
