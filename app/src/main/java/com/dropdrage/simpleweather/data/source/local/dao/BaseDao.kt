package com.dropdrage.simpleweather.data.source.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Has base DAO methods: [Insert], [Update], [Delete].
 */
interface BaseDao<T> {

    @Insert
    suspend fun insert(item: T)

    @Insert
    suspend fun insertAll(items: List<T>)

    @Update
    suspend fun updateAll(items: List<T>)

    @Delete
    suspend fun delete(items: T)

    @Delete
    suspend fun deleteAll(items: List<T>)
}