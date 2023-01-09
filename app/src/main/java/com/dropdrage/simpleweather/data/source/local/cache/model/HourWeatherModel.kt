package com.dropdrage.simpleweather.data.source.local.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dropdrage.simpleweather.data.source.local.cache.model.HourWeatherModel.Companion.DAY_ID_FIELD
import com.dropdrage.simpleweather.domain.weather.WeatherType
import java.time.LocalDateTime

private const val DATE_TIME_FIELD = "date_time"

@Entity(indices = [Index(DATE_TIME_FIELD, DAY_ID_FIELD, unique = true), Index(DAY_ID_FIELD)],
    foreignKeys = [ForeignKey(
        entity = DayWeatherModel::class,
        parentColumns = [DayWeatherModel.ID_FIELD],
        childColumns = [DAY_ID_FIELD],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HourWeatherModel(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = DATE_TIME_FIELD) val dateTime: LocalDateTime,
    @ColumnInfo(name = DAY_ID_FIELD) val dayId: Long,

    @ColumnInfo(name = "weather_type") val weatherType: WeatherType,
    @ColumnInfo(name = "temperature") val temperature: Float,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "wind_speed") val windSpeed: Float,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "visibility") val visibility: Float,
) {
    companion object {
        const val DAY_ID_FIELD = "day_id"
    }
}
