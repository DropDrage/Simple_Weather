package com.dropdrage.simpleweather.data.weather.test.local.cache.util

import com.dropdrage.simpleweather.data.weather.local.cache.util.LocalDateTimeUtils
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class LocalDateTimeUtilsTest {

    @Test
    fun `nowHourOnly minutes and seconds 0`() = mockkStatic(LocalDateTime::class) {
        every { LocalDateTime.now() } returns LocalDateTime.of(0, 1, 1, 1, 10, 10)

        val result = LocalDateTimeUtils.nowHourOnly

        assertEquals(0, result.minute)
        assertEquals(0, result.second)
    }

}
