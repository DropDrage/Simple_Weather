package com.dropdrage.simpleweather.city_search.presentation.model

import com.dropdrage.simpleweather.city.domain.City

internal data class ViewCitySearchResult(val city: City) {
    val name: String = city.name
    val countryName: String = city.country.name
}
