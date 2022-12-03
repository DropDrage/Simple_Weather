package com.dropdrage.simpleweather.data.util

import com.dropdrage.simpleweather.data.source.local.model.CityModel
import com.dropdrage.simpleweather.data.source.remote.dto.CitiesDto
import com.dropdrage.simpleweather.data.source.remote.dto.CityDto
import com.dropdrage.simpleweather.domain.city.Country
import com.dropdrage.simpleweather.domain.city.search.City
import com.dropdrage.simpleweather.domain.location.Location


fun CityModel.toDomain(): City = City(
    name = name,
    location = location,
    country = country,
)


fun CitiesDto.toDomainCities(): List<City> = result.map(CityDto::toDomainCity)

private fun CityDto.toDomainCity(): City = City(
    name = name,
    location = Location(latitude, longitude),
    country = Country(country, countryCode),
)


fun City.toModel(order: Int): CityModel = CityModel(
    name = name,
    country = country,
    location = location,
    order = order,
)