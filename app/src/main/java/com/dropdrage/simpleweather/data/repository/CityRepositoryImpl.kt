package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.source.local.dao.CityDao
import com.dropdrage.simpleweather.data.source.local.model.CityModel
import com.dropdrage.simpleweather.data.util.toDomain
import com.dropdrage.simpleweather.data.util.toModel
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.city.search.City
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val dao: CityDao) : CityRepository {
    override suspend fun getAllCitiesOrdered(): List<City> = dao.getAllOrdered().map(CityModel::toDomain)

    override suspend fun addCity(city: City) {
        val lastOrder = dao.getLastOrder() ?: -1
        dao.insert(city.toModel(lastOrder + 1))
    }

    override suspend fun updateCitiesOrder(orderedCities: List<City>) {
        val cities = dao.getAll().onEach {
            it.order = orderedCities.indexOfFirst { city -> it.equals(city) }
        }
        dao.updateOrders(cities)
    }


    override suspend fun deleteCity(city: City) {
        dao.delete(city.name, city.country.code, city.location.latitude, city.location.longitude)
    }
}