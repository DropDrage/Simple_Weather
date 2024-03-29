package com.dropdrage.simpleweather.feature.settings.presentation.util

import android.content.Context
import com.dropdrage.simpleweather.data.settings.WeatherUnit
import com.dropdrage.simpleweather.feature.settings.presentation.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class WeatherUnitConverter @Inject constructor(@ApplicationContext private val context: Context) {

    fun convertToSetting(unit: WeatherUnit): AnySetting = when (unit) {
        is DataTemperatureUnit -> ViewTemperatureUnit.fromData(unit)
        is DataPressureUnit -> ViewPressureUnit.fromData(unit)
        is DataWindSpeedUnit -> ViewWindSpeedUnit.fromData(unit)
        is DataVisibilityUnit -> ViewVisibilityUnit.fromData(unit)
        is DataPrecipitationUnit -> ViewPrecipitationUnit.fromData(unit)
    }


    fun convertToViewSetting(unit: WeatherUnit): ViewSetting {
        val setting = convertToSetting(unit)

        return ViewSetting(
            label = context.getString(setting.labelResId),
            currentValue = convertToValue(setting),
            values = setting.values,
        )
    }

    fun convertToValue(setting: AnySetting): String = context.getString(setting.unitResId, "")

}
