package com.dropdrage.simpleweather.presentation.model

import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.presentation.util.adapter.differ.SameEquatable

data class ViewCitySearchResult(
    val city: City,
) : SameEquatable<ViewCitySearchResult> {

    val name: String = city.name
    val countryName: String = city.country.name

    override fun isSame(other: ViewCitySearchResult): Boolean = this == other
}