package com.dropdrage.simpleweather.data.source.local.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dropdrage.simpleweather.data.source.local.CrudDao
import com.dropdrage.simpleweather.data.source.local.app.model.CityModel

@Dao
interface CityDao : CrudDao<CityModel> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insert(item: CityModel)

    @Query("SELECT * FROM CityModel WHERE `order` = :order LIMIT 1")
    suspend fun getWithOrder(order: Int): CityModel?

    @Query("SELECT * FROM CityModel")
    suspend fun getAll(): List<CityModel>

    @Query("SELECT * FROM CityModel ORDER BY `order`")
    suspend fun getAllOrdered(): List<CityModel>

    @Query("SELECT max(city.`order`) FROM CityModel city")
    suspend fun getLastOrder(): Int?


    @Query("DELETE FROM CityModel " +
        "WHERE name = :name AND country_code = :countryCode " +
        "AND location_latitude = :latitude AND location_longitude = :longitude")
    suspend fun delete(name: String, countryCode: String, latitude: Float, longitude: Float)


    @Transaction
    suspend fun updateOrders(orderedCities: List<CityModel>) {
        val citiesToResetOrders = orderedCities.onEach { it.order = -it.order - 1 }
        updateAll(citiesToResetOrders)
        val correctlyOrderedCities = orderedCities.onEach { it.order = -it.order - 1 }
        updateAll(correctlyOrderedCities)
    }

}
