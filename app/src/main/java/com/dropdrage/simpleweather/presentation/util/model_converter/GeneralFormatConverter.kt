package com.dropdrage.simpleweather.presentation.util.model_converter

import android.content.Context
import com.dropdrage.simpleweather.data.preferences.DateFormat
import com.dropdrage.simpleweather.data.preferences.GeneralFormat
import com.dropdrage.simpleweather.data.preferences.TimeFormat
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.model.ViewTimeFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GeneralFormatConverter @Inject constructor(@ApplicationContext private val context: Context) {

    fun convertToSetting(format: GeneralFormat): AnySetting = when (format) {
        is TimeFormat -> ViewTimeFormat.fromData(format)
        is DateFormat -> ViewDateFormat.fromData(format)
    }

    fun convertToViewSetting(format: GeneralFormat): ViewSetting {
        val viewGeneralFormat = convertToSetting(format)

        return ViewSetting(
            label = context.getString(viewGeneralFormat.labelResId),
            currentValue = context.getString(viewGeneralFormat.unitResId),
            values = viewGeneralFormat.values,
        )
    }

    fun convertToValue(generalFormat: AnySetting): String = context.getString(generalFormat.unitResId)

}