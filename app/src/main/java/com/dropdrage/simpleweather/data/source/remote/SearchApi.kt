package com.dropdrage.simpleweather.data.source.remote

import com.dropdrage.simpleweather.data.source.remote.dto.CitiesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun searchCities(@Query("name") query: String): CitiesDto
}