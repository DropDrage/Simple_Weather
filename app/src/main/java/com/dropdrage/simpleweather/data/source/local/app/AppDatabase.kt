package com.dropdrage.simpleweather.data.source.local.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dropdrage.simpleweather.data.source.local.app.dao.CityDao
import com.dropdrage.simpleweather.data.source.local.app.model.CityModel

@Database(entities = [CityModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val cityDao: CityDao


    companion object {
        const val DATABASE_NAME = "main-database"
    }

}