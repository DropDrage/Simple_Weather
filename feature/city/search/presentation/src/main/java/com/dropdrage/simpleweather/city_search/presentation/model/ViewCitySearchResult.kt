package com.dropdrage.simpleweather.city_search.presentation.model

import com.dropdrage.adapters.differ.SameEquatable

internal data class ViewCitySearchResult(val city: com.dropdrage.simpleweather.city_list.data.domain.City) :
    SameEquatable<ViewCitySearchResult> {

    val name: String = city.name
    val countryName: String = city.country.name

    override fun isSame(other: ViewCitySearchResult): Boolean = this == other

}
