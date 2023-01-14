package com.dropdrage.simpleweather.data.source.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Has base DAO methods: [Insert], [Update], [Delete].
 */
interface CrudDao<T> {

    @Insert
    suspend fun insert(item: T)

    @Insert
    suspend fun insertAll(items: List<T>)

    @Update
    suspend fun updateAll(items: List<T>)

    @Delete
    suspend fun delete(item: T)

    @Delete
    suspend fun deleteAll(items: List<T>)

}
