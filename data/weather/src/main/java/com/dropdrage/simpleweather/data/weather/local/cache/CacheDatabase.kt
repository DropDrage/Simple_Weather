package com.dropdrage.simpleweather.data.weather.local.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dropdrage.simpleweather.data.weather.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.HourWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.WeatherCacheDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.HourWeatherModel
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel
import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.WeatherTypeConverter
import com.dropdrage.simpleweather.data.weather.local.util.converter.LocalDateConverter
import com.dropdrage.simpleweather.data.weather.local.util.converter.LocalDateTimeConverter

@Database(
    entities = [LocationModel::class, HourWeatherModel::class, DayWeatherModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    WeatherTypeConverter::class,
)
internal abstract class CacheDatabase : RoomDatabase() {

    abstract val locationDao: LocationDao

    abstract val hourWeatherDao: HourWeatherDao
    abstract val dayWeatherDao: DayWeatherDao

    abstract val weatherCacheDao: WeatherCacheDao


    companion object {
        const val DATABASE_NAME = "cache-database"
    }

}
