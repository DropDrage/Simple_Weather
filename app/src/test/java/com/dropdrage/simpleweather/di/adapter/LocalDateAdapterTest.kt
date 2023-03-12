package com.dropdrage.simpleweather.di.adapter

import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LocalDateAdapterTest : BaseJsonAdapterTest() {

    private lateinit var dateAdapter: LocalDateAdapter


    @BeforeEach
    fun setUp() {
        dateAdapter = LocalDateAdapter()
    }


    @Test
    fun create() {
    }

    @Test
    fun `toJson null value`() {
        val writer = mockk<JsonWriter>(relaxed = true)

        dateAdapter.toJson(writer, null)

        verify(inverse = true) { writer.value(any<String>()) }
    }

    @Test
    fun `toJson not null value`() {
        val writer = mockk<JsonWriter>(relaxed = true)

        val date = LocalDate.now()
        dateAdapter.toJson(writer, date)

        verify { writer.value(date.format(LocalDateAdapter.FORMATTER)) }
    }

    @Test
    fun `fromJson peek NULL`() {
        val reader = createReaderWithPeekMock(JsonReader.Token.NULL)

        assertNull(dateAdapter.fromJson(reader))
        verify(exactly = 1) { reader.nextNull<Any>() }
    }

    @Test
    fun `fromJson peek not STRING`() {
        val reader = createReaderWithPeekMock(JsonReader.Token.NUMBER)

        val result = dateAdapter.fromJson(reader)

        assertNull(result)
        verify(exactly = 1) { reader.nextNull<Any>() }
    }

    @Test
    fun `fromJson peek STRING value `() {
        val date = LocalDate.now()
        val formattedDate = date.format(LocalDateAdapter.FORMATTER)
        val reader = createReaderWithPeekMock(JsonReader.Token.STRING)
        every { reader.nextString() } returns formattedDate

        val result = dateAdapter.fromJson(reader)

        assertEquals(date, result)
        verify(inverse = true) { reader.nextNull<Any>() }
    }

}
