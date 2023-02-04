package com.dropdrage.simpleweather.settings.presentation

import androidx.lifecycle.ViewModel
import com.dropdrage.simpleweather.data.settings.GeneralFormat
import com.dropdrage.simpleweather.data.settings.GeneralPreferences
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.settings.presentation.model.ViewPrecipitationUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewPressureUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewTimeFormat
import com.dropdrage.simpleweather.settings.presentation.model.ViewVisibilityUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewWindSpeedUnit
import com.dropdrage.simpleweather.settings.presentation.utils.GeneralFormatConverter
import com.dropdrage.simpleweather.settings.presentation.utils.WeatherUnitConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val weatherUnitConverter: WeatherUnitConverter,
    private val generalFormatConverter: GeneralFormatConverter,
) : ViewModel() {

    val settings: List<ViewSetting> = listOf(
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.temperatureUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.pressureUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.windSpeedUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.visibilityUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.precipitationUnit),
        generalFormatConverter.convertToViewSetting(GeneralPreferences.timeFormat),
        generalFormatConverter.convertToViewSetting(GeneralPreferences.dateFormat),
    )


    fun getCurrentValue(setting: AnySetting): AnySetting = when (setting) {
        is ViewTemperatureUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.temperatureUnit)
        is ViewPressureUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.pressureUnit)
        is ViewWindSpeedUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.windSpeedUnit)
        is ViewVisibilityUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.visibilityUnit)
        is ViewPrecipitationUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.precipitationUnit)
        is ViewTimeFormat -> generalFormatConverter.convertToSetting(GeneralPreferences.timeFormat)
        is ViewDateFormat -> generalFormatConverter.convertToSetting(GeneralPreferences.dateFormat)
    }

    fun changeSetting(setting: AnySetting) {
        when (setting) {
            is ViewTemperatureUnit -> WeatherUnitsPreferences.temperatureUnit = setting.toData()
            is ViewPressureUnit -> WeatherUnitsPreferences.pressureUnit = setting.toData()
            is ViewWindSpeedUnit -> WeatherUnitsPreferences.windSpeedUnit = setting.toData()
            is ViewVisibilityUnit -> WeatherUnitsPreferences.visibilityUnit = setting.toData()
            is ViewPrecipitationUnit -> WeatherUnitsPreferences.precipitationUnit = setting.toData()
            is ViewTimeFormat -> GeneralPreferences.timeFormat = setting.toData()
            is ViewDateFormat -> GeneralPreferences.dateFormat = setting.toData()
        }

        val changedSettingIndex = settings.indexOfFirst { it.values.contains(setting) }
        settings[changedSettingIndex].currentValue =
            if (setting is GeneralFormat) generalFormatConverter.convertToValue(setting)
            else weatherUnitConverter.convertToValue(setting)
    }
}