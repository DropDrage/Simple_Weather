package com.dropdrage.simpleweather.data.source.remote

import com.dropdrage.simpleweather.data.preferences.PrecipitationUnit
import com.dropdrage.simpleweather.data.preferences.TemperatureUnit
import com.dropdrage.simpleweather.data.preferences.WindSpeedUnit
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.source.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    suspend fun getWeather(
        @Query("latitude") latitude: Double, @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
        @Query("windspeed_unit") windSpeedUnit: WindSpeedUnit,
        @Query("precipitation_unit") precipitationUnit: PrecipitationUnit,
    ): WeatherResponseDto

    @GET("forecast?current_weather=true")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double, @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
    ): CurrentWeatherResponseDto
}