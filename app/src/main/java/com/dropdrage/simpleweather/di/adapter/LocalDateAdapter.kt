package com.dropdrage.simpleweather.di.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class LocalDateAdapter : JsonAdapter<LocalDate>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE


    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        value?.let { writer.value(it.format(formatter)) }
    }

    override fun fromJson(reader: JsonReader): LocalDate? =
        if (reader.peek() != JsonReader.Token.NULL) {
            fromNonNullString(reader.nextString())
        } else {
            reader.nextNull<Any>()
            null
        }

    private fun fromNonNullString(nextString: String): LocalDate = LocalDate.parse(nextString, formatter)

}