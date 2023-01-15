package com.dropdrage.simpleweather.presentation.util.model_converter

import android.content.Context
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.presentation.model.ViewTimeFormat
import com.dropdrage.simpleweather.settings.model.ViewSetting
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GeneralFormatConverter @Inject constructor(@ApplicationContext private val context: Context) {

    fun convertToSetting(format: com.dropdrage.simpleweather.settings_data.GeneralFormat): AnySetting = when (format) {
        is com.dropdrage.simpleweather.settings_data.TimeFormat -> ViewTimeFormat.fromData(format)
        is com.dropdrage.simpleweather.settings_data.DateFormat -> ViewDateFormat.fromData(format)
    }

    fun convertToViewSetting(format: com.dropdrage.simpleweather.settings_data.GeneralFormat): com.dropdrage.simpleweather.settings.model.ViewSetting {
        val viewGeneralFormat = convertToSetting(format)

        return com.dropdrage.simpleweather.settings.model.ViewSetting(
            label = context.getString(viewGeneralFormat.labelResId),
            currentValue = context.getString(viewGeneralFormat.unitResId),
            values = viewGeneralFormat.values,
        )
    }

    fun convertToValue(generalFormat: AnySetting): String = context.getString(generalFormat.unitResId)

}