package com.dropdrage.simpleweather.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME


    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        value?.let {
            writer.value(it.format(formatter))
        }
    }

    override fun fromJson(reader: JsonReader): LocalDateTime? =
        if (reader.peek() != JsonReader.Token.NULL) {
            fromNonNullString(reader.nextString())
        } else {
            reader.nextNull<Any>()
            null
        }

    private fun fromNonNullString(nextString: String): LocalDateTime = LocalDateTime.parse(nextString, formatter)

}