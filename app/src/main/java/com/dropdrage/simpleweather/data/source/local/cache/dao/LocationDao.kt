package com.dropdrage.simpleweather.data.source.local.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dropdrage.simpleweather.data.source.local.CrudDao
import com.dropdrage.simpleweather.data.source.local.cache.model.LocationModel

private const val POSITION_APPROXIMATION = 0.015f

@Dao
interface LocationDao : CrudDao<LocationModel> {

    @Insert
    suspend fun insertAndGetId(item: LocationModel): Long

    @Query("SELECT * FROM LocationModel " +
        "WHERE ABS(latitude - :latitude) < $POSITION_APPROXIMATION " +
        "AND ABS(longitude - :longitude) < $POSITION_APPROXIMATION ")
    suspend fun getLocationApproximately(latitude: Float, longitude: Float): LocationModel?

}
