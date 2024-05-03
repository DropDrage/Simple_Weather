package com.dropdrage.simpleweather.data.weather.local.cache.util.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

internal object LocalDateTimeConverter {

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun toLocalDateTime(seconds: Long?): LocalDateTime? =
        if (seconds != null) LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC)
        else null

}
