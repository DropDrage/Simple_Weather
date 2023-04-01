package com.dropdrage.simpleweather.city_list.presentation.presentation.ui

import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCityCurrentWeather
import com.dropdrage.simpleweather.city_list.presentation.presentation.model.ViewCurrentWeather
import com.dropdrage.test.util.verifyNever
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class CityCurrentWeatherDifferCallbackTest {

    private val differ = CityCurrentWeatherDifferCallback()


    @Test
    fun `getChangePayload not same then nothing`() {
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
    fun `getChangePayload same but currentWeather equals then nothing`() {
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
    fun `getChangePayload same and currentWeather different then nothing`() {
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
