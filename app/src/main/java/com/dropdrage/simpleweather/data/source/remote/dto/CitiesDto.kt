package com.dropdrage.simpleweather.data.source.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CitiesDto(@Json(name = "results") val result: List<CityDto> = emptyList())

data class CityDto(
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Float,
    @Json(name = "longitude") val longitude: Float,
    @Json(name = "country_code") val countryCode: String,
    @Json(name = "country") val country: String,
)
