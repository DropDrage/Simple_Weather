package com.dropdrage.simpleweather.data.source.local.util.converter

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalDateConverter {

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): Long? = localDate?.toEpochDay()

    @TypeConverter
    fun toLocalDate(day: Long?): LocalDate? =
        if (day != null) LocalDate.ofEpochDay(day)
        else null

}
