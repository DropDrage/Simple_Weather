package com.dropdrage.simpleweather.feature.city.search.presentation.model

import com.dropdrage.adapters.differ.SameEquatable
import com.dropdrage.simpleweather.feature.city.domain.City

internal data class ViewCitySearchResult(val city: City) :
    SameEquatable<ViewCitySearchResult> {

    val name: String = city.name
    val countryName: String = city.country.name

    override fun isSame(other: ViewCitySearchResult): Boolean = this == other

}
