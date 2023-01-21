package com.dropdrage.simpleweather.city_list.data.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dropdrage.simpleweather.city_list.data.domain.City
import com.dropdrage.simpleweather.city_list.data.domain.Country
import com.dropdrage.simpleweather.core.domain.Location

private const val NAME_FIELD = "name"
private const val ORDER_FIELD = "order"

private const val COUNTRY_PREFIX = "country_"
private const val COUNTRY_CODE = "${COUNTRY_PREFIX}code"

private const val LOCATION_PREFIX = "location_"
private const val LOCATION_LATITUDE = "${LOCATION_PREFIX}latitude"
private const val LOCATION_LONGITUDE = "${LOCATION_PREFIX}longitude"

@Entity(
    indices = [
        Index(value = [ORDER_FIELD], unique = true),
        Index(value = [NAME_FIELD, COUNTRY_CODE, LOCATION_LATITUDE, LOCATION_LONGITUDE], unique = true)
    ]
)
internal data class CityModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = NAME_FIELD) val name: String,
    @ColumnInfo(name = ORDER_FIELD) var order: Int,
    @Embedded(prefix = COUNTRY_PREFIX) val country: Country,
    @Embedded(prefix = LOCATION_PREFIX) val location: Location,
) {
    fun equals(other: City): Boolean = name == other.name && country == other.country && location == other.location
}