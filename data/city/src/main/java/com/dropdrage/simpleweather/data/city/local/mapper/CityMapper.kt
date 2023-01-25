package com.dropdrage.simpleweather.data.source.local.app.util.mapper

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.data.city.local.model.CityModel

internal fun CityModel.toDomain(): City = City(
    name = name,
    location = location,
    country = country,
)

internal fun City.toModel(order: Int): CityModel = CityModel(
    name = name,
    country = country,
    location = location,
    order = order,
)