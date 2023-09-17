package com.dropdrage.simpleweather.di.adapter

import com.dropdrage.test.util.justMock
import com.dropdrage.test.util.verifyNever
import com.dropdrage.test.util.verifyOnce
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class LocalDateTimeAdapterTest : BaseJsonAdapterTest() {

    private val dateTimeAdapter: LocalDateTimeAdapter = LocalDateTimeAdapter()


    @Test
    fun create() {
    }

    @Nested
    inner class toJson {

        @Test
        fun `null value`() {
            val writer = mockk<JsonWriter>()

            dateTimeAdapter.toJson(writer, null)

            verifyNever { writer.value(any<String>()) }
        }

        @Test
        fun `not null value`() {
            val writer = mockk<JsonWriter> {
                justMock { value(any<String>()) }
            }
            val date = LocalDateTime.now()

            dateTimeAdapter.toJson(writer, date)

            verifyOnce { writer.value(date.format(LocalDateTimeAdapter.FORMATTER)) }
        }

    }

    @Nested
    inner class fromJson {

        @Test
        fun `peek NULL`() {
            val reader = createReaderWithPeekMock(JsonReader.Token.NULL)
            justRun { reader.nextNull<Any>() }

            val result = dateTimeAdapter.fromJson(reader)

            verifyOnce { reader.nextNull<Any>() }
            assertNull(result)
        }

        @Test
        fun `peek not STRING`() {
            val reader = createReaderWithPeekMock(JsonReader.Token.NUMBER)
            justRun { reader.nextNull<Any>() }

            val result = dateTimeAdapter.fromJson(reader)

            verifyOnce { reader.nextNull<Any>() }
            assertNull(result)
        }

        @Test
        fun `peek STRING value `() {
            val date = LocalDateTime.now()
            val formattedDate = date.format(LocalDateTimeAdapter.FORMATTER)
            val reader = createReaderWithPeekMock(JsonReader.Token.STRING)
            every { reader.nextString() } returns formattedDate

            val result = dateTimeAdapter.fromJson(reader)

            verifyNever { reader.nextNull<Any>() }
            assertEquals(date, result)
        }

    }

}
