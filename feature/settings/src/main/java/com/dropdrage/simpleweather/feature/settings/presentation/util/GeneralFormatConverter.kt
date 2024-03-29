package com.dropdrage.simpleweather.feature.settings.presentation.util

import android.content.Context
import com.dropdrage.simpleweather.data.settings.DateFormat
import com.dropdrage.simpleweather.data.settings.GeneralFormat
import com.dropdrage.simpleweather.data.settings.TimeFormat
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewTimeFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class GeneralFormatConverter @Inject constructor(@ApplicationContext private val context: Context) {

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
