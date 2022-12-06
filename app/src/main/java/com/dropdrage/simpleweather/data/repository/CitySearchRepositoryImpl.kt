package com.dropdrage.simpleweather.data.repository

import com.dropdrage.simpleweather.data.source.remote.SearchApi
import com.dropdrage.simpleweather.data.util.LogTags
import com.dropdrage.simpleweather.data.util.mapper.toDomainCities
import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.domain.city.search.CitySearchRepository
import com.dropdrage.simpleweather.domain.util.Resource
import javax.inject.Inject

class CitySearchRepositoryImpl @Inject constructor(private val api: SearchApi) :
    SimpleRepository<List<City>>(LogTags.SEARCH), CitySearchRepository {
    override suspend fun searchCities(query: String): Resource<List<City>> = simplyResourceWrap {
        api.searchCities(query).toDomainCities()
    }
}