package com.dropdrage.simpleweather.domain.city

import com.dropdrage.simpleweather.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    val orderedCities: Flow<List<City>>


    suspend fun getCityWithOrder(order: Int): Resource<City>

    suspend fun getAllCitiesOrdered(): List<City>

    suspend fun addCity(city: City)

    suspend fun updateCitiesOrders(orderedCities: List<City>)

    suspend fun deleteCity(city: City)

}
