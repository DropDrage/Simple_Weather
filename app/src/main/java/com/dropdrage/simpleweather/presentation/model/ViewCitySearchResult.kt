package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.city.City

data class ViewCitySearchResult(
    val city: City,
) : com.dropdrage.adapters.differ.SameEquatable<ViewCitySearchResult> {

    val name: String = city.name
    val countryName: String = city.country.name

    override fun isSame(other: ViewCitySearchResult): Boolean = this == other
}