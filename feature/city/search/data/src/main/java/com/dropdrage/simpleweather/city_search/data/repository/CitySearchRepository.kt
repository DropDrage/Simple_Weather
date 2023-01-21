package com.dropdrage.simpleweather.city_search.data.repository

import com.dropdrage.simpleweather.common.domain.Resource

interface CitySearchRepository {
    suspend fun searchCities(query: String): Resource<List<com.dropdrage.simpleweather.city_list.data.domain.City>>
}
