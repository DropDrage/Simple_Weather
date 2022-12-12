package com.dropdrage.simpleweather.presentation.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropdrage.simpleweather.data.preferences.GeneralFormat
import com.dropdrage.simpleweather.data.preferences.GeneralPreferences
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
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
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.temperatureUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.pressureUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.windSpeedUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.visibilityUnit),
        weatherUnitConverter.convertToViewSetting(WeatherUnitsPreferences.precipitationUnit),
        generalFormatConverter.convertToViewSetting(GeneralPreferences.timeFormat),
        generalFormatConverter.convertToViewSetting(GeneralPreferences.dateFormat),
    )

    private val _settingChanged = MutableLiveData<ViewSetting>()
    val settingChanged: LiveData<ViewSetting> = _settingChanged


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

        _settingChanged.value = settings[changedSettingIndex]
    }
}