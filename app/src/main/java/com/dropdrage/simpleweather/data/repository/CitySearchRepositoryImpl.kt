package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.source.remote.SearchApi
import com.dropdrage.simpleweather.data.util.toDomainCities
import com.dropdrage.simpleweather.domain.search.City
import com.dropdrage.simpleweather.domain.search.CitySearchRepository
import com.dropdrage.simpleweather.domain.util.Resource
import javax.inject.Inject

class CitySearchRepositoryImpl @Inject constructor(private val api: SearchApi) : CitySearchRepository {
    override suspend fun searchCities(query: String): Resource<List<City>> = try {
        val result = api.searchCities(query)
        Resource.Success(result.toDomainCities())
    } catch (e: Exception) {
        Resource.Error(e.message, e)
    }
}