package com.dropdrage.simpleweather.feature.city.search.domain

import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city.domain.City

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}
