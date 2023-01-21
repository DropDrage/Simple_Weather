package com.dropdrage.simpleweather.city_search.data.remote

import com.dropdrage.simpleweather.core.domain.Location

internal fun CitiesDto.toDomainCities(): List<com.dropdrage.simpleweather.city_list.data.domain.City> =
    result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): com.dropdrage.simpleweather.city_list.data.domain.City =
    com.dropdrage.simpleweather.city_list.data.domain.City(
        name = name,
        location = Location(latitude, longitude),
        country = com.dropdrage.simpleweather.city_list.data.domain.Country(country, countryCode),
    )
