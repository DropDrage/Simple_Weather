package com.dropdrage.simpleweather.city_search.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchApi {
    @GET("search")
    suspend fun searchCities(@Query("name") query: String): CitiesDto
}