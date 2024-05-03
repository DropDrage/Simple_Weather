package com.dropdrage.simpleweather.feature.city.list.presentation.ui

import com.dropdrage.common.test.util.verifyNever
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.feature.city.list.presentation.model.ViewCurrentWeather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CityCurrentWeatherDifferCallbackTest {

    private val differ = CityCurrentWeatherDifferCallback()


    @Nested
    inner class getChangePayload {

        @Test
        fun `not same then nothing`() {
            val newItem = mockk<ViewCityCurrentWeather>()
            val oldItem = mockk<ViewCityCurrentWeather> {
                every { isSame(eq(newItem)) } returns false
            }

            val payload = differ.getChangePayload(oldItem, newItem)

            verify { oldItem.isSame(eq(newItem)) }
            verifyNever {
                oldItem.currentWeather
                newItem.currentWeather
            }
            assertNull(payload)
        }

        @Test
        fun `same but currentWeather equals then nothing`() {
            val weather = mockk<ViewCurrentWeather>()
            val newItem = mockk<ViewCityCurrentWeather> {
                every { currentWeather } returns weather
            }
            val oldItem = mockk<ViewCityCurrentWeather> {
                every { isSame(eq(newItem)) } returns true
                every { currentWeather } returns weather
            }

            val payload = differ.getChangePayload(oldItem, newItem)

            verify {
                oldItem.isSame(eq(newItem))
                oldItem.currentWeather
                newItem.currentWeather
            }
            assertNull(payload)
        }

        @Test
        fun `same and currentWeather different then nothing`() {
            val newWeather = mockk<ViewCurrentWeather>()
            val newItem = mockk<ViewCityCurrentWeather> {
                every { currentWeather } returns newWeather
            }
            val oldWeather = mockk<ViewCurrentWeather>()
            val oldItem = mockk<ViewCityCurrentWeather> {
                every { isSame(eq(newItem)) } returns true
                every { currentWeather } returns oldWeather
            }

            val payload = differ.getChangePayload(oldItem, newItem)

            verify {
                oldItem.isSame(eq(newItem))
                oldItem.currentWeather
                newItem.currentWeather
            }
            assertEquals(newWeather, payload)
        }

    }

}
