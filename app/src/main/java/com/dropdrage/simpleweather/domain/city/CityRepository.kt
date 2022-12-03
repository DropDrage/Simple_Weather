package com.dropdrage.simpleweather.domain.city

import com.dropdrage.simpleweather.domain.city.search.City

interface CityRepository {
    suspend fun getAllCitiesOrdered(): List<City>

    suspend fun addCity(city: City)

    suspend fun updateCitiesOrder(orderedCities: List<City>)

    suspend fun deleteCity(city: City)
}