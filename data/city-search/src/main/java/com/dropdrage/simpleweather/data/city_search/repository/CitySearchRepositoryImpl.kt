package com.dropdrage.simpleweather.data.city_search.repository

import com.dropdrage.simpleweather.common.data.repository.SimpleRepository
import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.core.data.LogTags
import com.dropdrage.simpleweather.data.city.domain.City
import com.dropdrage.simpleweather.data.city_search.remote.SearchApi
import com.dropdrage.simpleweather.data.city_search.remote.toDomainCities
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.annotations.BoundTo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BoundTo(supertype = CitySearchRepository::class, component = SingletonComponent::class)
internal class CitySearchRepositoryImpl @Inject constructor(private val api: SearchApi) :
    SimpleRepository<List<City>>(LogTags.SEARCH), CitySearchRepository {
    override suspend fun searchCities(query: String): Resource<List<City>> = simplyResourceWrap {
        api.searchCities(query).toDomainCities()
    }
}
