package com.dropdrage.simpleweather.data.weather.test.di

import com.dropdrage.simpleweather.data.weather.di.ApiSupportedParamFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ApiSupportedParamConverterTest {

    @Test
    fun `convert correct`() {
        val apiParamValue = "apiParam"
        val param = mockk<FakeApiSupportedParam>() {
            every { apiParam } returns apiParamValue
        }

        val result = ApiSupportedParamFactory.ApiSupportedParamConverter.convert(param)

        verify { param.apiParam }
        assertEquals(apiParamValue, result)
    }

}
