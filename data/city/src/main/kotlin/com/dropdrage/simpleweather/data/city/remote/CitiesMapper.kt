package com.dropdrage.simpleweather.data.city.remote

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.feature.city.domain.City
import com.dropdrage.simpleweather.feature.city.domain.Country

internal fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    country = Country(country, countryCode),
)
