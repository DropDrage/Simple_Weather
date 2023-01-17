package com.dropdrage.simpleweather.data.weather.local.cache.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dropdrage.simpleweather.common.data.CrudDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.relation.DayToHourWeather
import java.time.LocalDate

@Dao
internal interface DayWeatherDao : CrudDao<DayWeatherModel> {

    @Transaction
    @Query(
        "SELECT * FROM DayWeatherModel day_weather " +
            "WHERE day_weather.location_id = :locationId AND day_weather.date >= :startDate "
    )
    suspend fun getWeatherForLocationFromDay(locationId: Long, startDate: LocalDate): List<DayToHourWeather>


    @Query("SELECT COUNT(*) > 0 FROM DayWeatherModel WHERE location_id = :locationId")
    suspend fun hasWeatherForLocation(locationId: Long): Boolean


    @Query("DELETE FROM DayWeatherModel WHERE location_id = :locationId AND date < :currentDate")
    suspend fun deleteOutdatedForLocation(locationId: Long, currentDate: LocalDate): Int

}
