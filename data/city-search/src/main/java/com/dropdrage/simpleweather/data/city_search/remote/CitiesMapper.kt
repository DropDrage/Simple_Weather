package com.dropdrage.simpleweather.data.city_search.remote

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.city.domain.City
import com.dropdrage.simpleweather.data.city.domain.Country

internal fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City =
    City(
        name = name,
        location = Location(latitude, longitude),
        country = Country(country, countryCode),
    )
