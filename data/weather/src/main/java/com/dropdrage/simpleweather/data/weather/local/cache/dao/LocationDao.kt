package com.dropdrage.simpleweather.data.weather.local.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dropdrage.common.data.CrudDao
import com.dropdrage.simpleweather.data.weather.local.cache.model.LocationModel

private const val POSITION_APPROXIMATION = 0.015f

@Dao
internal interface LocationDao : CrudDao<LocationModel> {

    @Insert
    suspend fun insertAndGetId(item: LocationModel): Long


    @Query(
        "SELECT * FROM LocationModel " +
            "WHERE ABS(latitude - :latitude) < $POSITION_APPROXIMATION " +
            "AND ABS(longitude - :longitude) < $POSITION_APPROXIMATION "
    )
    suspend fun getLocationApproximately(latitude: Float, longitude: Float): LocationModel?

    @Query("SELECT id FROM LocationModel")
    suspend fun getAllIds(): List<Long>


    @Query("SELECT COUNT(*) > 0 FROM LocationModel")
    suspend fun hasItems(): Boolean


    @Query("DELETE FROM LocationModel WHERE id = :id")
    suspend fun delete(id: Long)

}
