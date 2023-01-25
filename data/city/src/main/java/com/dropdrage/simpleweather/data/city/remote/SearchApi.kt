package com.dropdrage.simpleweather.data.city.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchApi {
    @GET("search")
    suspend fun searchCities(@Query("name") query: String): CitiesDto
}