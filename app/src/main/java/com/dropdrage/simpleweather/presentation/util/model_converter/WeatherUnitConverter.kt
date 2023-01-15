package com.dropdrage.simpleweather.presentation.util.model_converter

import android.content.Context
import com.dropdrage.simpleweather.presentation.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherUnitConverter @Inject constructor(@ApplicationContext private val context: Context) {

    fun convertToSetting(unit: com.dropdrage.simpleweather.settings_data.WeatherUnit): AnySetting = when (unit) {
        is DataPrecipitationUnit -> ViewPrecipitationUnit.fromData(unit)
        is DataPressureUnit -> ViewPressureUnit.fromData(unit)
        is DataTemperatureUnit -> ViewTemperatureUnit.fromData(unit)
        is DataVisibilityUnit -> ViewVisibilityUnit.fromData(unit)
        is DataWindSpeedUnit -> ViewWindSpeedUnit.fromData(unit)
    }


    fun convertToViewSetting(unit: com.dropdrage.simpleweather.settings_data.WeatherUnit): com.dropdrage.simpleweather.settings.model.ViewSetting {
        val setting: AnySetting = convertToSetting(unit)

        return com.dropdrage.simpleweather.settings.model.ViewSetting(
            label = context.getString(setting.labelResId),
            currentValue = convertToValue(setting),
            values = setting.values,
        )
    }

    fun convertToValue(setting: AnySetting): String = context.getString(setting.unitResId, "")

}