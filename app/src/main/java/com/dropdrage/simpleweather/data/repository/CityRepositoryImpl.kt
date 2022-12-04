package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.source.local.dao.CityDao
import com.dropdrage.simpleweather.data.source.local.model.CityModel
import com.dropdrage.simpleweather.data.util.toDomain
import com.dropdrage.simpleweather.data.util.toModel
import com.dropdrage.simpleweather.domain.city.CityRepository
import com.dropdrage.simpleweather.domain.city.search.City
import com.dropdrage.simpleweather.domain.util.Resource
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val dao: CityDao) : CityRepository {

    override suspend fun getCityWithOrder(order: Int): Resource<City> = try {
        Resource.Success(dao.getWithOrder(order)!!.toDomain())
    } catch (e: Exception) {
        Resource.Error(e.message, e)
    }

    override suspend fun getAllCitiesOrdered(): List<City> = dao.getAllOrdered().map(CityModel::toDomain)

    override suspend fun addCity(city: City) {
        val lastOrder = dao.getLastOrder() ?: -1
        dao.insert(city.toModel(lastOrder + 1))
    }

    override suspend fun updateCitiesOrders(orderedCities: List<City>) {
        val cities = dao.getAll().onEach {
            it.order = orderedCities.indexOfFirst { city -> it.equals(city) }
        }
        dao.updateOrders(cities)
    }


    override suspend fun deleteCity(city: City) {
        dao.delete(city.name, city.country.code, city.location.latitude, city.location.longitude)
        compressOrders()
    }

    private suspend fun compressOrders() {
        val orderedCities = dao.getAllOrdered()
        if (orderedCities.isEmpty()) {
            return
        }

        val cityWithCompressedOrders = orderedCities.onEachIndexed { i, city -> city.order = i }
        dao.updateOrders(cityWithCompressedOrders)
    }

}