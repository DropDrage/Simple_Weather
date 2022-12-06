package com.dropdrage.simpleweather.domain.city

import com.dropdrage.simpleweather.domain.util.Resource

interface CityRepository {
    suspend fun getCityWithOrder(order: Int): Resource<City>

    suspend fun getAllCitiesOrdered(): List<City>

    suspend fun addCity(city: City)

    suspend fun updateCitiesOrders(orderedCities: List<City>)

    suspend fun deleteCity(city: City)
}