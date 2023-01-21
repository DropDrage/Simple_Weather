package com.dropdrage.simpleweather.weather.data.local.cache.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dropdrage.simpleweather.weather.data.local.cache.model.DayWeatherModel
import com.dropdrage.simpleweather.weather.data.local.cache.model.HourWeatherModel

internal data class DayToHourWeather(
    @Embedded val day: DayWeatherModel,
    @Relation(
        parentColumn = DayWeatherModel.ID_FIELD,
        entityColumn = HourWeatherModel.DAY_ID_FIELD,
    ) val hours: List<HourWeatherModel>,
)
