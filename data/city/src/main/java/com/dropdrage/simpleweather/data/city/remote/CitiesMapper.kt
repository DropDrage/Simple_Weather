package com.dropdrage.simpleweather.data.city.remote

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location

internal fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    country = Country(country, countryCode),
)
