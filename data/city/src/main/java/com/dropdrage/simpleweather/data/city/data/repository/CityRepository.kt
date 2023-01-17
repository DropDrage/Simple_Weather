package com.dropdrage.simpleweather.data.city.data.repository

import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.data.city.domain.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    val orderedCities: Flow<List<City>>


    suspend fun addCity(city: City)

    suspend fun getCityWithOrder(order: Int): Resource<City>

    suspend fun getAllCitiesOrdered(): List<City>

    suspend fun updateCitiesOrders(orderedCities: List<City>)

    suspend fun deleteCity(city: City)

}
