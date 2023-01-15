package com.dropdrage.simpleweather.settings.presentation.utils

import android.content.Context
import com.dropdrage.simpleweather.settings.data.DateFormat
import com.dropdrage.simpleweather.settings.data.GeneralFormat
import com.dropdrage.simpleweather.settings.data.TimeFormat
import com.dropdrage.simpleweather.settings.model.AnySetting
import com.dropdrage.simpleweather.settings.model.ViewDateFormat
import com.dropdrage.simpleweather.settings.model.ViewTimeFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GeneralFormatConverter @Inject constructor(@ApplicationContext private val context: Context) {

    fun convertToSetting(format: GeneralFormat): AnySetting = when (format) {
        is TimeFormat -> ViewTimeFormat.fromData(format)
        is DateFormat -> ViewDateFormat.fromData(format)
    }

    fun convertToViewSetting(format: GeneralFormat): com.dropdrage.simpleweather.settings.model.ViewSetting {
        val viewGeneralFormat = convertToSetting(format)

        return com.dropdrage.simpleweather.settings.model.ViewSetting(
            label = context.getString(viewGeneralFormat.labelResId),
            currentValue = context.getString(viewGeneralFormat.unitResId),
            values = viewGeneralFormat.values,
        )
    }

    fun convertToValue(generalFormat: AnySetting): String = context.getString(generalFormat.unitResId)

}
