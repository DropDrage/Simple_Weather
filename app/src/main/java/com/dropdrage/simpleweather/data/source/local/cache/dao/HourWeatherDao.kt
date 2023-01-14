package com.dropdrage.simpleweather.data.source.local.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.dropdrage.simpleweather.data.source.local.CrudDao
import com.dropdrage.simpleweather.data.source.local.cache.dto.CurrentWeatherDto
import com.dropdrage.simpleweather.data.source.local.cache.model.HourWeatherModel
import java.time.LocalDateTime

@Dao
interface HourWeatherDao : CrudDao<HourWeatherModel> {

    @Query("SELECT temperature, hour_weather.weather_type as weatherType FROM HourWeatherModel hour_weather " +
        "JOIN DayWeatherModel day_weather ON day_weather.id = hour_weather.day_id " +
        "WHERE day_weather.location_id = :locationId AND hour_weather.date_time = :currentDateTime")
    suspend fun getCurrentWeather(locationId: Long, currentDateTime: LocalDateTime): CurrentWeatherDto?

}
