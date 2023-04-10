package com.dropdrage.simpleweather.data.location.test.util

import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.location.util.mockLocation
import com.dropdrage.simpleweather.data.util.mapper.toDomainLocation
import com.dropdrage.simpleweather.data.util.mapper.toLocationResult
import com.dropdrage.simpleweather.weather.domain.location.LocationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.jupiter.api.Test

internal class LocationMapperTest {

    @Test
    fun `toDomainLocation correct`() {
        val latitude = 1.0
        val longitude = 2.0
        val location = mockLocation(latitude, longitude)

        val result = location.toDomainLocation()

        assertThat(result).isInstanceOf(Location::class.java)
        assertEquals(latitude.toFloat(), result.latitude)
        assertEquals(longitude.toFloat(), result.longitude)
    }

    @Test
    fun `toLocationResult null returns NoLocation`() {
        val result = null.toLocationResult()

        assertSame(LocationResult.NoLocation, result)
    }

    @Test
    fun `toLocationResult not null returns Success`() {
        val latitude = 1.0
        val longitude = 2.0
        val location = mockLocation(latitude, longitude)

        val result = location.toLocationResult()

        assertThat(result).isInstanceOf(LocationResult.Success::class.java)
        val resultLocation = (result as LocationResult.Success).location
        assertEquals(latitude.toFloat(), resultLocation.latitude)
        assertEquals(longitude.toFloat(), resultLocation.longitude)
    }

}
