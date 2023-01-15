package com.dropdrage.simpleweather.domain.city.search

import com.dropdrage.simpleweather.core.domain.Resource
import com.dropdrage.simpleweather.domain.city.City

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}