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
import java.time.LocalDate

internal class LocalDateAdapterTest : BaseJsonAdapterTest() {

    private val dateAdapter: LocalDateAdapter = LocalDateAdapter()


    @Test
    fun create() {
    }


    @Nested
    inner class toJson {

        @Test
        fun `toJson null value`() {
            val writer = mockk<JsonWriter>()

            dateAdapter.toJson(writer, null)

            verifyNever { writer.value(any<String>()) }
        }

        @Test
        fun `toJson not null value`() {
            val writer = mockk<JsonWriter> {
                justMock { value(any<String>()) }
            }
            val date = LocalDate.now()

            dateAdapter.toJson(writer, date)

            verifyOnce { writer.value(date.format(LocalDateAdapter.FORMATTER)) }
        }

    }

    @Nested
    inner class fromJson {

        @Test
        fun `fromJson peek NULL`() {
            val reader = createReaderWithPeekMock(JsonReader.Token.NULL)
            justRun { reader.nextNull<Any>() }

            val result = dateAdapter.fromJson(reader)

            verifyOnce { reader.nextNull<Any>() }
            assertNull(result)
        }

        @Test
        fun `fromJson peek not STRING`() {
            val reader = createReaderWithPeekMock(JsonReader.Token.NUMBER)
            justRun { reader.nextNull<Any>() }

            val result = dateAdapter.fromJson(reader)

            verifyOnce { reader.nextNull<Any>() }
            assertNull(result)
        }

        @Test
        fun `fromJson peek STRING value`() {
            val date = LocalDate.now()
            val formattedDate = date.format(LocalDateAdapter.FORMATTER)
            val reader = createReaderWithPeekMock(JsonReader.Token.STRING)
            every { reader.nextString() } returns formattedDate

            val result = dateAdapter.fromJson(reader)

            verifyNever { reader.nextNull<Any>() }
            assertEquals(date, result)
        }

    }

}
