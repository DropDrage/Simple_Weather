package com.dropdrage.simpleweather.data.source.local.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

private const val LATITUDE_FIELD = "latitude"
private const val LONGITUDE_FIELD = "longitude"

@Entity(indices = [Index(LATITUDE_FIELD, LONGITUDE_FIELD, unique = true)])
data class LocationModel(
    @ColumnInfo(name = ID_FIELD) @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = LATITUDE_FIELD) val latitude: Float,
    @ColumnInfo(name = LONGITUDE_FIELD) val longitude: Float,

    @ColumnInfo(name = "update_time") val updateTime: LocalDateTime? = LocalDateTime.now(),
) {
    companion object {
        const val ID_FIELD = "id"
    }
}
