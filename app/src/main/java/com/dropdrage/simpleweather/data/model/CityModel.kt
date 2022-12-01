package com.dropdrage.simpleweather.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dropdrage.simpleweather.domain.location.Location

@Entity
data class CityModel(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @Embedded(prefix = "country_") val country: Country,
    @Embedded(prefix = "location_") val location: Location,
)

data class Country(val name: String, val code: String)