package com.dropdrage.simpleweather.data.weather.local.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dropdrage.simpleweather.common.domain.Range
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import java.time.LocalDate
import java.time.LocalDateTime

private const val DATE_FIELD = "date"
private const val LOCATION_ID_FIELD = "location_id"

@Entity(
    indices = [Index(DATE_FIELD, LOCATION_ID_FIELD, unique = true), Index(LOCATION_ID_FIELD)],
    foreignKeys = [ForeignKey(
        entity = LocationModel::class,
        parentColumns = [LocationModel.ID_FIELD],
        childColumns = [LOCATION_ID_FIELD],
        onDelete = ForeignKey.CASCADE
    )]
)
internal data class DayWeatherModel(
    @ColumnInfo(name = ID_FIELD) @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = DATE_FIELD) val date: LocalDate,
    @ColumnInfo(name = LOCATION_ID_FIELD) val locationId: Long,

    @ColumnInfo(name = "weather_type") val weatherType: WeatherType,
    @Embedded(prefix = "temperature_") val temperatureRange: TemperatureRange,
    @Embedded(prefix = "temperature_apparent_") val apparentTemperatureRange: TemperatureRange,
    @ColumnInfo(name = "precipitation_sum") val precipitationSum: Float,
    @ColumnInfo(name = "max_wind_speed") val maxWindSpeed: Float,
    @ColumnInfo(name = "sunrise") val sunrise: LocalDateTime,
    @ColumnInfo(name = "sunset") val sunset: LocalDateTime,
) {
    companion object {
        const val ID_FIELD = "id"
    }
}

internal data class TemperatureRange(val start: Float, val end: Float) {

    fun toRange(): Range<Float> = Range(start, end)

    companion object {
        fun fromRange(range: Range<Float>) = TemperatureRange(range.start, range.end)
    }

}
