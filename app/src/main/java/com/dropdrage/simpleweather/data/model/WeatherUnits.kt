package com.dropdrage.simpleweather.data.model

import androidx.annotation.StringRes
import com.dropdrage.simpleweather.R

sealed interface WeatherUnit{
    @get:StringRes
    val unitResId: Int
}

enum class TemperatureUnit(@StringRes override val unitResId: Int): WeatherUnit {
    CELSIUS(R.string.weather_unit_temperature_celsius), FAHRENHEIT(R.string.weather_unit_temperature_fahrenheit),
}

enum class PressureUnit(@StringRes override val unitResId: Int): WeatherUnit {
    H_PASCAL(R.string.weather_unit_pressure_pa), MM_HG(R.string.weather_unit_pressure_mm_hg)
}

enum class WindSpeedUnit(@StringRes override val unitResId: Int): WeatherUnit {
    KM_H(R.string.weather_unit_wind_speed_km_h), MPH(R.string.weather_unit_wind_speed_mph)
}