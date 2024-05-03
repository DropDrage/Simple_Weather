package com.dropdrage.simpleweather.di.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {

    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        value?.let { writer.value(it.format(FORMATTER)) }
    }

    override fun fromJson(reader: JsonReader): LocalDateTime? =
        if (reader.peek() == JsonReader.Token.STRING) {
            fromNonNullString(reader.nextString())
        } else {
            reader.nextNull<Any>()
            null
        }

    private fun fromNonNullString(nextString: String): LocalDateTime = LocalDateTime.parse(nextString, FORMATTER)


    companion object {
        val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

}
