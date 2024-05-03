package com.dropdrage.simpleweather.data.weather.remote

import com.dropdrage.simpleweather.data.settings.PrecipitationUnit
import com.dropdrage.simpleweather.data.settings.TemperatureUnit
import com.dropdrage.simpleweather.data.settings.WindSpeedUnit
import com.dropdrage.simpleweather.data.source.remote.dto.CurrentWeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

private const val HOURLY_PARAMS = "temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl,visibility"

private const val DAILY_PARAMS = "weathercode,temperature_2m_min,temperature_2m_max," +
    "apparent_temperature_min,apparent_temperature_max," +
    "precipitation_sum,windspeed_10m_max," +
    "sunrise,sunset"

internal interface WeatherApi {

    @GET("forecast?hourly=$HOURLY_PARAMS&daily=$DAILY_PARAMS")
    suspend fun getWeather(
        @Query("latitude") latitude: Float, @Query("longitude") longitude: Float,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
        @Query("windspeed_unit") windSpeedUnit: WindSpeedUnit,
        @Query("precipitation_unit") precipitationUnit: PrecipitationUnit,
        @Query("timezone") timezone: String,
    ): WeatherResponseDto

    @GET("forecast?current_weather=true")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Float, @Query("longitude") longitude: Float,
        @Query("temperature_unit") temperatureUnit: TemperatureUnit,
    ): CurrentWeatherResponseDto

}
