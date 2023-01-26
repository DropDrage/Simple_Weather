package com.dropdrage.simpleweather.data.city.repository

import com.dropdrage.common.data.repository.SimpleRepository
import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.CityRepository
import com.dropdrage.simpleweather.core.utils.LogTags
import com.dropdrage.simpleweather.data.city.local.dao.CityDao
import com.dropdrage.simpleweather.data.city.local.model.CityModel
import com.dropdrage.simpleweather.data.source.local.app.util.mapper.toDomain
import com.dropdrage.simpleweather.data.source.local.app.util.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject internal constructor(
    private val dao: CityDao,
) : SimpleRepository<City>(LogTags.CITY), CityRepository {

    override val orderedCities: Flow<List<City>> = dao.getAllOrderedList().map { it.map(CityModel::toDomain) }


    override suspend fun addCity(city: City) {
        val lastOrder = dao.getLastOrder() ?: -1
        dao.insert(city.toModel(lastOrder + 1))
    }

    override suspend fun getCityWithOrder(order: Int): Resource<City> = simplyResourceWrap {
        dao.getWithOrder(order)!!.toDomain()
    }

    override suspend fun getAllCitiesOrdered(): List<City> = dao.getAllOrdered().map(CityModel::toDomain)

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
