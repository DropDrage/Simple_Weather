package com.dropdrage.simpleweather.data.source.remote

import com.dropdrage.simpleweather.data.preferences.PrecipitationUnit
import com.dropdrage.simpleweather.data.preferences.TemperatureUnit
import com.dropdrage.simpleweather.data.preferences.WindSpeedUnit
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import com.dropdrage.simpleweather.data.source.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val HOURLY_PARAMS = "temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl,visibility"

private const val DAILY_PARAMS = "weathercode,temperature_2m_min,temperature_2m_max," +
    "apparent_temperature_min,apparent_temperature_max," +
    "precipitation_sum,windspeed_10m_max," +
    "sunrise,sunset"

interface WeatherApi {
    @GET("forecast?hourly=$HOURLY_PARAMS&daily=$DAILY_PARAMS")
    suspend fun getWeather(
        @Query("latitude") latitude: Double, @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
        @Query("windspeed_unit") windSpeedUnit: WindSpeedUnit,
        @Query("precipitation_unit") precipitationUnit: PrecipitationUnit,
        @Query("timezone") timezone: String,
    ): WeatherResponseDto

    @GET("forecast?current_weather=true")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double, @Query("longitude") longitude: Double,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
    ): CurrentWeatherResponseDto
}