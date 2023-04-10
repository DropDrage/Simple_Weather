package com.dropdrage.simpleweather.data.weather.test.local.cache.util.converter

import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.LocalDateConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LocalDateConverterTest {

    @Test
    fun `fromLocalDate null`() {
        val result = LocalDateConverter.fromLocalDate(null)

        assertNull(result)
    }

    @Test
    fun `fromLocalDate not null returns days`() {
        val epochDay = 50L
        val date = LocalDate.ofEpochDay(epochDay)

        val result = LocalDateConverter.fromLocalDate(date)

        assertEquals(epochDay, result)
    }


    @Test
    fun `toLocalDate null`() {
        val result = LocalDateConverter.toLocalDate(null)

        assertNull(result)
    }

    @Test
    fun `toLocalDate not null returns days`() {
        val epochDay = 50L
        val date = LocalDate.ofEpochDay(epochDay)

        val result = LocalDateConverter.toLocalDate(epochDay)

        assertEquals(date, result)
    }

}
