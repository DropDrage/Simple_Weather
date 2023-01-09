package com.dropdrage.simpleweather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.data.source.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "Cache"

class CacheRepository @Inject constructor(
    private val locationDao: LocationDao,
    private val dayWeatherDao: DayWeatherDao,
) {

    suspend fun clearOutdated() {
        withContext(Dispatchers.IO) {
            val allLocations = locationDao.getAll()
            val currentDate = LocalDate.now()
            allLocations.asFlow()
                .map { it.id!! }
                .onEach { locationId ->
                    val deleted = dayWeatherDao.deleteOutdatedForLocation(locationId, currentDate)
                    Log.d(TAG, "$deleted days have been deleted for $locationId")
                }
                .collect { locationId ->
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
