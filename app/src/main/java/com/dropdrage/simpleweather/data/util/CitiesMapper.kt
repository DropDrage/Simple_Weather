package com.dropdrage.simpleweather.data.util

import com.dropdrage.simpleweather.data.dto.CitiesDto
import com.dropdrage.simpleweather.data.dto.CityDto
import com.dropdrage.simpleweather.data.model.CityModel
import com.dropdrage.simpleweather.data.model.Country
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.search.City

fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    countryCode = countryCode,
    country = country,
)


fun City.toModel(): CityModel = CityModel(
    name = name,
    country = Country(country, countryCode),
    location = location
)