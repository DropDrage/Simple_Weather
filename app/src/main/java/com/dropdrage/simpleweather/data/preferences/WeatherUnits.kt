package com.dropdrage.simpleweather.data.preferences

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.dropdrage.simpleweather.R
import java.time.format.DateTimeFormatter

sealed interface WeatherUnit {
    @get:StringRes
    val unitResId: Int
}

private const val NO_PLURAL = 0

interface CanBePluralUnit {
    val isPlural: Boolean
        get() = unitPluralResId != NO_PLURAL

    @get:PluralsRes
    val unitPluralResId: Int
}


enum class TemperatureUnit(@StringRes override val unitResId: Int) : WeatherUnit {
    CELSIUS(R.string.weather_unit_temperature_celsius), FAHRENHEIT(R.string.weather_unit_temperature_fahrenheit),
}

enum class PressureUnit(@StringRes override val unitResId: Int) : WeatherUnit {
    H_PASCAL(R.string.weather_unit_pressure_pa), MM_HG(R.string.weather_unit_pressure_mm_hg)
}

enum class WindSpeedUnit(
    @StringRes override val unitResId: Int,
    override val unitPluralResId: Int = NO_PLURAL,
) : WeatherUnit, CanBePluralUnit {
    M_S(R.string.weather_unit_wind_speed_m_s),
    KM_H(R.string.weather_unit_wind_speed_km_h),
    MPH(R.string.weather_unit_wind_speed_mph),
    KNOTS(R.string.weather_unit_wind_speed_knot, R.plurals.weather_unit_wind_speed_knots)
}

enum class VisibilityUnit(
    @StringRes override val unitResId: Int,
    override val unitPluralResId: Int = NO_PLURAL,
) : WeatherUnit, CanBePluralUnit {
    METER(R.string.weather_unit_visibility_meter),
    K_METER(R.string.weather_unit_visibility_kmeter),
    MILE(R.string.weather_unit_visibility_mile, R.plurals.weather_unit_visibility_miles),
}

enum class PrecipitationUnit(
    @StringRes override val unitResId: Int,
    override val unitPluralResId: Int = NO_PLURAL,
) : WeatherUnit, CanBePluralUnit {
    MM(R.string.weather_unit_precipitation_mm),
    INCH(R.string.weather_unit_precipitation_inch, R.plurals.weather_unit_precipitation_inches),
}

enum class TimeFormat(format: String) {
    H_12("h a"), H_24("HH:00");

    val formatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern(format)
    }
}