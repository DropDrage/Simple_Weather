package com.dropdrage.simpleweather.city_search.data.remote

import com.dropdrage.simpleweather.city_list.domain.city.City
import com.dropdrage.simpleweather.city_list.domain.city.Country
import com.dropdrage.simpleweather.core.domain.Location

internal fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    country = Country(country, countryCode),
)