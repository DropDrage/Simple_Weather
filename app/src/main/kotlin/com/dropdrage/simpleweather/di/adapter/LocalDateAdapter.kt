package com.dropdrage.simpleweather.di.adapter

import androidx.annotation.VisibleForTesting
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class LocalDateAdapter : JsonAdapter<LocalDate>() {

    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        value?.let { writer.value(it.format(FORMATTER)) }
    }

    override fun fromJson(reader: JsonReader): LocalDate? =
        if (reader.peek() == JsonReader.Token.STRING) {
            fromNonNullString(reader.nextString())
        } else {
            reader.nextNull<Any>()
            null
        }

    private fun fromNonNullString(nextString: String): LocalDate = LocalDate.parse(nextString, FORMATTER)


    companion object {
        @VisibleForTesting
        val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
    }

}
