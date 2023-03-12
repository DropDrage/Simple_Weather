package com.dropdrage.simpleweather.di.adapter

import com.squareup.moshi.JsonReader
import io.mockk.every
import io.mockk.mockk

abstract class BaseJsonAdapterTest {
    protected fun createReaderWithPeekMock(peekReturn: JsonReader.Token): JsonReader = mockk(relaxed = true) {
        every { peek() } returns peekReturn
    }
}