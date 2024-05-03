package com.dropdrage.simpleweather.data.weather.test.local.cache.util.mapper

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.weather.local.cache.util.mapper.toNewModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class LocationMapperTest {

    @Test
    fun `toNewModel success`() {
        val location = Location(1f, 2f)

        val result = location.toNewModel()

        assertNull(result.id)
        assertEquals(location.latitude, result.latitude)
        assertEquals(location.longitude, result.longitude)
        assertNotNull(result.updateTime)
    }

}
