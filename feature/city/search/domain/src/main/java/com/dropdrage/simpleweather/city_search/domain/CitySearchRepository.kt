package com.dropdrage.simpleweather.city_search.domain

import com.dropdrage.common.domain.Resource
import com.dropdrage.simpleweather.city_list.domain.city.City

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}
