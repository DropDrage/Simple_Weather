package com.dropdrage.simpleweather.domain.search

import com.dropdrage.simpleweather.domain.util.Resource

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<City>>
}