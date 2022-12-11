package com.dropdrage.simpleweather.data.preferences

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.dropdrage.simpleweather.R
import com.dropdrage.simpleweather.data.util.ApiSupportedParam

sealed interface WeatherUnit {
    @get:StringRes
    val unitResId: Int
}

private const val NOT_PLURAL = 0

interface CanBePluralUnit {
    @get:StringRes
    val unitResId: Int

    val isNotPlural: Boolean
        get() = unitPluralResId == NOT_PLURAL

    @get:PluralsRes
    val unitPluralResId: Int
}


enum class TemperatureUnit(
    @StringRes override val unitResId: Int,
    override val apiParam: String,
) : WeatherUnit, ApiSupportedParam {
    CELSIUS(R.string.weather_unit_temperature_celsius, "celsius"),
    FAHRENHEIT(R.string.weather_unit_temperature_fahrenheit, "fahrenheit"),
}

enum class PressureUnit(@StringRes override val unitResId: Int) : WeatherUnit {
    H_PASCAL(R.string.weather_unit_pressure_pa), MM_HG(R.string.weather_unit_pressure_mm_hg)
}

enum class WindSpeedUnit(
    @StringRes override val unitResId: Int,
    override val apiParam: String,
    override val unitPluralResId: Int = NOT_PLURAL,
) : WeatherUnit, ApiSupportedParam, CanBePluralUnit {
    M_S(R.string.weather_unit_wind_speed_m_s, "ms"),
    KM_H(R.string.weather_unit_wind_speed_km_h, "kmh"),
    MPH(R.string.weather_unit_wind_speed_mph, "mph"),
    KNOTS(R.string.weather_unit_wind_speed_knot, "kn", R.plurals.weather_unit_wind_speed_knots)
}

enum class VisibilityUnit(
    @StringRes override val unitResId: Int,
    override val unitPluralResId: Int = NOT_PLURAL,
) : WeatherUnit, CanBePluralUnit {
    METER(R.string.weather_unit_visibility_meter),
    K_METER(R.string.weather_unit_visibility_kmeter),
    MILE(R.string.weather_unit_visibility_mile, R.plurals.weather_unit_visibility_miles),
}

enum class PrecipitationUnit(
    @StringRes override val unitResId: Int,
    override val apiParam: String,
    override val unitPluralResId: Int = NOT_PLURAL,
) : WeatherUnit, ApiSupportedParam, CanBePluralUnit {
    MM(R.string.weather_unit_precipitation_mm, "mm"),
    INCH(R.string.weather_unit_precipitation_inch, "inch", R.plurals.weather_unit_precipitation_inches),
}

