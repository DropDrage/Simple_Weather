package com.dropdrage.simpleweather.data.city_search.repository

import com.dropdrage.simpleweather.common.domain.Resource
import com.dropdrage.simpleweather.data.city.domain.City

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}
