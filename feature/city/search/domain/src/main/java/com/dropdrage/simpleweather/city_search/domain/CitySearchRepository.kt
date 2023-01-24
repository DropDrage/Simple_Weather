package com.dropdrage.simpleweather.city_search.domain

import com.dropdrage.simpleweather.city_list.domain.city.City
import com.dropdrage.simpleweather.common.domain.Resource

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}
