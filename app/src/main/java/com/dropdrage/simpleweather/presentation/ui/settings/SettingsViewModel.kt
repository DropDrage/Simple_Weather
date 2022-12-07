package com.dropdrage.simpleweather.presentation.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropdrage.simpleweather.data.preferences.DateTimePreferences
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.presentation.model.AnySetting
import com.dropdrage.simpleweather.presentation.model.Setting
import com.dropdrage.simpleweather.presentation.model.ViewPrecipitationUnit
import com.dropdrage.simpleweather.presentation.model.ViewPressureUnit
import com.dropdrage.simpleweather.presentation.model.ViewSetting
import com.dropdrage.simpleweather.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.presentation.model.ViewTimeFormat
import com.dropdrage.simpleweather.presentation.model.ViewVisibilityUnit
import com.dropdrage.simpleweather.presentation.model.ViewWindSpeedUnit
import com.dropdrage.simpleweather.presentation.util.model_converter.WeatherUnitConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val weatherUnitConverter: WeatherUnitConverter) : ViewModel() {

    private val _settings = MutableLiveData<List<ViewSetting>>()
    val settings: LiveData<List<ViewSetting>> = _settings


    fun updateSettings() {
        _settings.value = listOf(
            weatherUnitConverter.convertToView(WeatherUnitsPreferences.temperatureUnit),
            weatherUnitConverter.convertToView(WeatherUnitsPreferences.pressureUnit),
            weatherUnitConverter.convertToView(WeatherUnitsPreferences.windSpeedUnit),
            weatherUnitConverter.convertToView(WeatherUnitsPreferences.visibilityUnit),
            weatherUnitConverter.convertToView(WeatherUnitsPreferences.precipitationUnit),
            weatherUnitConverter.convertToView(DateTimePreferences.timeFormat),
        )
    }

    fun getCurrentValue(setting: AnySetting): AnySetting = when (setting) {
        is ViewTemperatureUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.temperatureUnit)
        is ViewPressureUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.pressureUnit)
        is ViewWindSpeedUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.windSpeedUnit)
        is ViewVisibilityUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.visibilityUnit)
        is ViewPrecipitationUnit -> weatherUnitConverter.convertToSetting(WeatherUnitsPreferences.precipitationUnit)
        is ViewTimeFormat -> weatherUnitConverter.convertToSetting(DateTimePreferences.timeFormat)
    }

    fun changeSetting(setting: Setting<*, *>) {
        when (setting) {
            is ViewTemperatureUnit -> WeatherUnitsPreferences.temperatureUnit = setting.toData()
            is ViewPressureUnit -> WeatherUnitsPreferences.pressureUnit = setting.toData()
            is ViewWindSpeedUnit -> WeatherUnitsPreferences.windSpeedUnit = setting.toData()
            is ViewVisibilityUnit -> WeatherUnitsPreferences.visibilityUnit = setting.toData()
            is ViewPrecipitationUnit -> WeatherUnitsPreferences.precipitationUnit = setting.toData()
            is ViewTimeFormat -> DateTimePreferences.timeFormat = setting.toData()
        }
    }
}