package com.dropdrage.simpleweather.data.source.local.app.util.mapper

import com.dropdrage.simpleweather.data.source.local.app.model.CityModel
import com.dropdrage.simpleweather.domain.city.City


fun CityModel.toDomain(): City = City(
    name = name,
    location = location,
    country = country,
)

fun City.toModel(order: Int): CityModel = CityModel(
    name = name,
    country = country,
    location = location,
    order = order,
)
