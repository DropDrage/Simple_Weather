package com.dropdrage.simpleweather.data.city.local.mapper

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.city.local.model.CityModel
import com.dropdrage.simpleweather.data.source.local.app.util.mapper.toDomain
import com.dropdrage.simpleweather.data.source.local.app.util.mapper.toModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.jupiter.api.Test

internal class CityMapperTest {

    @Test
    fun `CityModel toDomain`() {
        val cityModel = CityModel(1, "name", 1, Country("Country", "CY"), Location(1f, 2f))

        val result = cityModel.toDomain()

        assertEquals(cityModel.name, result.name)
        assertEquals(cityModel.country, result.country)
        assertEquals(cityModel.location, result.location)
    }

    @Test
    fun `City toModel`() {
        val city = City("City", Location(1f, 2f), Country("Country", "CY"))
        val order = 1

        val result = city.toModel(order)

        assertEquals(city.name, result.name)
        assertEquals(city.country, result.country)
        assertEquals(city.location, result.location)
        assertEquals(order, result.order)
        assertNull(result.id)
    }

}
