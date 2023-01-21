package com.dropdrage.simpleweather.city_search.data.repository

import com.dropdrage.simpleweather.city_search.data.remote.SearchApi
import com.dropdrage.simpleweather.city_search.data.remote.toDomainCities
import com.dropdrage.simpleweather.common.data.repository.SimpleRepository
import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.core.data.LogTags
import dagger.hilt.components.SingletonComponent
import it.czerwinski.android.hilt.annotations.BoundTo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BoundTo(supertype = CitySearchRepository::class, component = SingletonComponent::class)
internal class CitySearchRepositoryImpl @Inject constructor(private val api: SearchApi) :
    SimpleRepository<List<com.dropdrage.simpleweather.city_list.data.domain.City>>(LogTags.SEARCH),
    CitySearchRepository {
    override suspend fun searchCities(query: String): Resource<List<com.dropdrage.simpleweather.city_list.data.domain.City>> =
        simplyResourceWrap {
            api.searchCities(query).toDomainCities()
        }
}
