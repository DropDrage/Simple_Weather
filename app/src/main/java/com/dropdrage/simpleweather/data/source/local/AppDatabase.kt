package com.dropdrage.simpleweather.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dropdrage.simpleweather.data.model.CityModel
import com.dropdrage.simpleweather.data.source.local.dao.CityDao

@Database(entities = [CityModel::class], version = 1)
//@TypeConverters(TimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}