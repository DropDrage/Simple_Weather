package com.dropdrage.simpleweather.data.city.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dropdrage.simpleweather.data.city.local.dao.CityDao
import com.dropdrage.simpleweather.data.city.local.model.CityModel

@Database(entities = [CityModel::class], version = 1, exportSchema = false)
internal abstract class CityDatabase : RoomDatabase() {

    abstract val cityDao: CityDao


    companion object {
        const val DATABASE_NAME = "city-database"
    }

}