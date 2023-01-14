package com.dropdrage.simpleweather.data.source.local.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dropdrage.simpleweather.data.source.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.source.local.cache.model.HourWeatherModel
import java.time.LocalDateTime

@Dao
abstract class WeatherCacheDao {

    @Insert
    protected abstract suspend fun insertAllDayWeathersAndGetIds(items: List<DayWeatherModel>): List<Long>

    @Insert
    protected abstract suspend fun insertAllHourWeathers(items: List<HourWeatherModel>)


    @Query("UPDATE LocationModel SET update_time = :updateTime WHERE id = :id")
    protected abstract suspend fun updateLocationUpdateTime(id: Long, updateTime: LocalDateTime)


    @Query("DELETE FROM DayWeatherModel WHERE location_id = :locationId")
    protected abstract suspend fun clearForLocation(locationId: Long)


    @Transaction
    open suspend fun updateWeather(
        locationId: Long,
        dayWeathers: List<DayWeatherModel>,
        getHourWeathers: (List<Long>) -> List<HourWeatherModel>,
    ) {
        val now = LocalDateTime.now()
        val daysIds = clearAndInsertWeather(locationId, dayWeathers)
        insertAllHourWeathers(getHourWeathers(daysIds))
        updateLocationUpdateTime(locationId, now)
    }

    @Transaction
    protected open suspend fun clearAndInsertWeather(locationId: Long, dayWeathers: List<DayWeatherModel>): List<Long> {
        clearForLocation(locationId)
        return insertAllDayWeathersAndGetIds(dayWeathers)
    }

}
