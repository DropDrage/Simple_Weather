package com.dropdrage.simpleweather.data.weather.repository

import android.util.Log
import com.dropdrage.simpleweather.data.weather.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.weather.local.cache.dao.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "Cache"

class CacheRepository @Inject internal constructor(
    private val locationDao: LocationDao,
    private val dayWeatherDao: DayWeatherDao,
) {

    suspend fun clearOutdated() {
        withContext(Dispatchers.IO) {
            val deleted = dayWeatherDao.deleteOutdatedForAllLocations(LocalDate.now())
            Log.d(TAG, "$deleted days have been deleted for all")

            locationDao.getAllIds().forEach { locationId ->
                val isExistWeatherForLocation = dayWeatherDao.hasWeatherForLocation(locationId)
                if (!isExistWeatherForLocation) {
                    Log.d(TAG, "$locationId location has been deleted")
                    locationDao.delete(locationId)
                }
            }
        }
    }

    suspend fun hasCache(): Boolean = locationDao.hasItems()

}
