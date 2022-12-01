package com.dropdrage.simpleweather.data.source.remote

import com.dropdrage.simpleweather.data.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    suspend fun getWeather(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double) : WeatherDto
}