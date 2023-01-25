package com.dropdrage.simpleweather.data.city.repository

import com.dropdrage.common.data.repository.SimpleRepository
import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city_search.domain.CitySearchRepository
import com.dropdrage.simpleweather.core.utils.LogTags
import com.dropdrage.simpleweather.data.city.remote.SearchApi
import com.dropdrage.simpleweather.data.city.remote.toDomainCities
import javax.inject.Inject

class CitySearchRepositoryImpl @Inject internal constructor(private val api: SearchApi) :
    SimpleRepository<List<City>>(LogTags.SEARCH), CitySearchRepository {
    override suspend fun searchCities(query: String): Resource<List<City>> = simplyResourceWrap {
        api.searchCities(query).toDomainCities()
    }
}