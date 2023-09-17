package com.dropdrage.simpleweather.data.weather.test.di

import com.dropdrage.simpleweather.data.weather.di.ApiSupportedParamFactory
import io.mockk.mockk
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ApiSupportedParamFactoryTest {

    private var factory: ApiSupportedParamFactory = ApiSupportedParamFactory()


    @Nested
    inner class `stringConverter type` {

        @Test
        fun `is ApiSupportedParam`() {
            val result = factory.stringConverter(FakeApiSupportedParam::class.java, emptyArray(), mockk())

            assertSame(ApiSupportedParamFactory.ApiSupportedParamConverter, result)
        }

        @Test
        fun `is not ApiSupportedParam`() {
            val result = factory.stringConverter(Any::class.java, emptyArray(), mockk())

            assertNotSame(ApiSupportedParamFactory.ApiSupportedParamConverter, result)
        }

    }

}
