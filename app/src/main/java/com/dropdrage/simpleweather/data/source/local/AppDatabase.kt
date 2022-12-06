package com.dropdrage.simpleweather.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dropdrage.simpleweather.data.source.local.dao.CityDao
import com.dropdrage.simpleweather.data.source.local.model.CityModel

@Database(entities = [CityModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val cityDao: CityDao


    companion object {
        const val DATABASE_NAME = "main-database"
    }
}