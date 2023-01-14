package com.dropdrage.simpleweather.data.util.mapper

import com.dropdrage.simpleweather.data.source.remote.dto.CitiesDto
import com.dropdrage.simpleweather.data.source.remote.dto.CityDto
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.domain.city.Country
import com.dropdrage.simpleweather.domain.location.Location

fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    country = Country(country, countryCode),
)
