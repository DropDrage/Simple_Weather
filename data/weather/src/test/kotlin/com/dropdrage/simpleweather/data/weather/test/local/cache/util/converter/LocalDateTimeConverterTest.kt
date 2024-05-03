package com.dropdrage.simpleweather.data.weather.test.local.cache.util.converter

import com.dropdrage.simpleweather.data.weather.local.cache.util.converter.LocalDateTimeConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class LocalDateTimeConverterTest {

    @Nested
    inner class fromLocalDateTime {

        @Test
        fun `input null then returns null`() {
            val result = LocalDateTimeConverter.fromLocalDateTime(null)

            assertNull(result)
        }

        @Test
        fun `input not null then returns days`() {
            val epochSecond = 500000L
            val dateTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC)

            val result = LocalDateTimeConverter.fromLocalDateTime(dateTime)

            assertEquals(epochSecond, result)
        }

    }

    @Nested
    inner class toLocalDateTime {

        @Test
        fun `input null then returns null`() {
            val result = LocalDateTimeConverter.toLocalDateTime(null)

            assertNull(result)
        }

        @Test
        fun `input not null then returns days`() {
            val epochSecond = 500000L
            val dateTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.UTC)

            val result = LocalDateTimeConverter.toLocalDateTime(epochSecond)

            assertEquals(dateTime, result)
        }

    }

}
