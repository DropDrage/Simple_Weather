package com.dropdrage.simpleweather.domain.city.search

import com.dropdrage.simpleweather.domain.city.City
import com.dropdrage.simpleweather.domain.util.Resource

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}