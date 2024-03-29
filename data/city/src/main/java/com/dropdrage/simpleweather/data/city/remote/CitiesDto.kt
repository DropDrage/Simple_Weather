package com.dropdrage.simpleweather.data.city.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CitiesDto(@Json(name = "results") val result: List<CityDto> = emptyList())

internal data class CityDto(
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Float,
    @Json(name = "longitude") val longitude: Float,
    @Json(name = "country_code") val countryCode: String,
    @Json(name = "country") val country: String,
)
