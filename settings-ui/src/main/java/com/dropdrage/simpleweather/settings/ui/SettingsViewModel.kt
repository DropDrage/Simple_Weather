package com.dropdrage.simpleweather.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.model.ViewDateFormat
import com.dropdrage.simpleweather.presentation.model.ViewPrecipitationUnit
import com.dropdrage.simpleweather.presentation.model.ViewPressureUnit
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.presentation.model.ViewTimeFormat
import com.dropdrage.simpleweather.presentation.model.ViewVisibilityUnit
import com.dropdrage.simpleweather.presentation.model.ViewWindSpeedUnit
import com.dropdrage.simpleweather.presentation.util.model_converter.GeneralFormatConverter
import com.dropdrage.simpleweather.presentation.util.model_converter.WeatherUnitConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val weatherUnitConverter: WeatherUnitConverter,
    private val generalFormatConverter: GeneralFormatConverter,
) : ViewModel() {

    val settings: List<ViewSetting> = listOf(
        weatherUnitConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.temperatureUnit),
        weatherUnitConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.pressureUnit),
        weatherUnitConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.windSpeedUnit),
        weatherUnitConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.visibilityUnit),
        weatherUnitConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.precipitationUnit),
        generalFormatConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.GeneralPreferences.timeFormat),
        generalFormatConverter.convertToViewSetting(com.dropdrage.simpleweather.settings_data.GeneralPreferences.dateFormat),
    )

    private val _settingChanged = MutableLiveData<ViewSetting>()
    val settingChanged: LiveData<ViewSetting> = _settingChanged


    fun getCurrentValue(setting: AnySetting): AnySetting = when (setting) {
        is ViewTemperatureUnit -> weatherUnitConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.temperatureUnit)
        is ViewPressureUnit -> weatherUnitConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.pressureUnit)
        is ViewWindSpeedUnit -> weatherUnitConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.windSpeedUnit)
        is ViewVisibilityUnit -> weatherUnitConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.visibilityUnit)
        is ViewPrecipitationUnit -> weatherUnitConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.precipitationUnit)
        is ViewTimeFormat -> generalFormatConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.GeneralPreferences.timeFormat)
        is ViewDateFormat -> generalFormatConverter.convertToSetting(com.dropdrage.simpleweather.settings_data.GeneralPreferences.dateFormat)
    }

    fun changeSetting(setting: AnySetting) {
        when (setting) {
            is ViewTemperatureUnit -> com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.temperatureUnit =
                setting.toData()
            is ViewPressureUnit -> com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.pressureUnit =
                setting.toData()
            is ViewWindSpeedUnit -> com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.windSpeedUnit =
                setting.toData()
            is ViewVisibilityUnit -> com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.visibilityUnit =
                setting.toData()
            is ViewPrecipitationUnit -> com.dropdrage.simpleweather.settings_data.WeatherUnitsPreferences.precipitationUnit =
                setting.toData()
            is ViewTimeFormat -> com.dropdrage.simpleweather.settings_data.GeneralPreferences.timeFormat =
                setting.toData()
            is ViewDateFormat -> com.dropdrage.simpleweather.settings_data.GeneralPreferences.dateFormat =
                setting.toData()
        }

        val changedSettingIndex = settings.indexOfFirst { it.values.contains(setting) }
        settings[changedSettingIndex].currentValue =
            if (setting is com.dropdrage.simpleweather.settings_data.GeneralFormat) generalFormatConverter.convertToValue(
                setting
            )
            else weatherUnitConverter.convertToValue(setting)

        _settingChanged.value = settings[changedSettingIndex]
    }
}