package com.dropdrage.simpleweather.settings.model

import androidx.annotation.StringRes
import com.dropdrage.simpleweather.settings_data.R
import com.dropdrage.simpleweather.settings_data.TemperatureUnit
import com.dropdrage.simpleweather.settings_data.WindSpeedUnit

typealias AnySetting = Setting<*, *>

sealed interface Setting<out T : Setting<T, D>, out D> {
    @get:StringRes
    val unitResId: Int

    @get:StringRes
    val labelResId: Int

    val values: List<T>


    fun toData(): D
}


typealias DataTemperatureUnit = TemperatureUnit

enum class ViewTemperatureUnit(@StringRes override val unitResId: Int) :
    Setting<ViewTemperatureUnit, DataTemperatureUnit> {
    CELSIUS(R.string.weather_unit_temperature_celsius), FAHRENHEIT(R.string.weather_unit_temperature_fahrenheit);

    override val labelResId: Int = R.string.weather_unit_temperature_label

    override val values: List<ViewTemperatureUnit>
        get() = values().toList()


    override fun toData() = when (this) {
        CELSIUS -> DataTemperatureUnit.CELSIUS
        FAHRENHEIT -> DataTemperatureUnit.FAHRENHEIT
    }

    companion object {
        fun fromData(unit: DataTemperatureUnit) = when (unit) {
            DataTemperatureUnit.CELSIUS -> CELSIUS
            DataTemperatureUnit.FAHRENHEIT -> FAHRENHEIT
        }
    }
}


typealias DataPressureUnit = com.dropdrage.simpleweather.settings_data.PressureUnit

enum class ViewPressureUnit(@StringRes override val unitResId: Int) : Setting<ViewPressureUnit, DataPressureUnit> {
    H_PASCAL(R.string.weather_unit_pressure_pa), MM_HG(R.string.weather_unit_pressure_mm_hg);

    override val labelResId: Int = R.string.weather_unit_pressure_label

    override val values: List<ViewPressureUnit>
        get() = values().toList()


    override fun toData() = when (this) {
        H_PASCAL -> DataPressureUnit.H_PASCAL
        MM_HG -> DataPressureUnit.MM_HG
    }

    companion object {
        fun fromData(unit: DataPressureUnit) = when (unit) {
            DataPressureUnit.H_PASCAL -> H_PASCAL
            DataPressureUnit.MM_HG -> MM_HG
        }
    }
}


typealias DataWindSpeedUnit = WindSpeedUnit

enum class ViewWindSpeedUnit(@StringRes override val unitResId: Int) : Setting<ViewWindSpeedUnit, DataWindSpeedUnit> {
    M_S(R.string.weather_unit_wind_speed_m_s),
    KM_H(R.string.weather_unit_wind_speed_km_h),
    MPH(R.string.weather_unit_wind_speed_mph),
    KNOTS(R.string.weather_unit_wind_speed_knot);

    override val labelResId: Int = R.string.weather_unit_wind_speed_label

    override val values: List<ViewWindSpeedUnit>
        get() = values().toList()


    override fun toData(): DataWindSpeedUnit = when (this) {
        M_S -> DataWindSpeedUnit.M_S
        KM_H -> DataWindSpeedUnit.KM_H
        MPH -> DataWindSpeedUnit.MPH
        KNOTS -> DataWindSpeedUnit.KNOTS
    }

    companion object {
        fun fromData(unit: DataWindSpeedUnit) = when (unit) {
            DataWindSpeedUnit.M_S -> M_S
            DataWindSpeedUnit.KM_H -> KM_H
            DataWindSpeedUnit.MPH -> MPH
            DataWindSpeedUnit.KNOTS -> KNOTS
        }
    }
}


typealias DataVisibilityUnit = com.dropdrage.simpleweather.settings_data.VisibilityUnit

enum class ViewVisibilityUnit(@StringRes override val unitResId: Int) :
    Setting<ViewVisibilityUnit, DataVisibilityUnit> {
    METER(R.string.weather_unit_visibility_meter),
    K_METER(R.string.weather_unit_visibility_kmeter),
    MILE(R.string.weather_unit_visibility_mile);

    override val labelResId: Int = R.string.weather_unit_visibility_label

    override val values: List<ViewVisibilityUnit>
        get() = values().toList()


    override fun toData(): DataVisibilityUnit = when (this) {
        METER -> DataVisibilityUnit.METER
        K_METER -> DataVisibilityUnit.K_METER
        MILE -> DataVisibilityUnit.MILE
    }

    companion object {
        fun fromData(unit: DataVisibilityUnit) = when (unit) {
            DataVisibilityUnit.METER -> METER
            DataVisibilityUnit.K_METER -> K_METER
            DataVisibilityUnit.MILE -> MILE
        }
    }
}


typealias DataPrecipitationUnit = com.dropdrage.simpleweather.settings_data.PrecipitationUnit

enum class ViewPrecipitationUnit(
    @StringRes override val unitResId: Int,
) : Setting<ViewPrecipitationUnit, DataPrecipitationUnit> {
    MM(R.string.weather_unit_precipitation_mm),
    INCH(R.string.weather_unit_precipitation_inch);

    override val labelResId: Int = R.string.weather_unit_precipitation_label

    override val values: List<ViewPrecipitationUnit>
        get() = values().toList()


    override fun toData(): DataPrecipitationUnit = when (this) {
        MM -> DataPrecipitationUnit.MM
        INCH -> DataPrecipitationUnit.INCH
    }

    companion object {
        fun fromData(unit: DataPrecipitationUnit) = when (unit) {
            DataPrecipitationUnit.MM -> MM
            DataPrecipitationUnit.INCH -> INCH
        }
    }
}


typealias DataTimeFormat = com.dropdrage.simpleweather.settings_data.TimeFormat

enum class ViewTimeFormat(@StringRes override val unitResId: Int) : Setting<ViewTimeFormat, DataTimeFormat> {
    H_12(R.string.time_format_12),
    H_24(R.string.time_format_24);

    override val labelResId: Int = R.string.time_format_label

    override val values: List<ViewTimeFormat>
        get() = values().toList()


    override fun toData(): DataTimeFormat = when (this) {
        H_12 -> DataTimeFormat.H_12
        H_24 -> DataTimeFormat.H_24
    }

    companion object {
        fun fromData(unit: DataTimeFormat) = when (unit) {
            DataTimeFormat.H_12 -> H_12
            DataTimeFormat.H_24 -> H_24
        }
    }
}

typealias DataDateFormat = com.dropdrage.simpleweather.settings_data.DateFormat

enum class ViewDateFormat(@StringRes override val unitResId: Int) : Setting<ViewDateFormat, DataDateFormat> {
    REVERSED(R.string.date_format_reversed), STRAIGHT(R.string.date_format_straight);

    override val labelResId: Int = R.string.date_format_label

    override val values: List<ViewDateFormat>
        get() = values().toList()


    override fun toData(): DataDateFormat = when (this) {
        REVERSED -> DataDateFormat.REVERSED
        STRAIGHT -> DataDateFormat.STRAIGHT
    }

    companion object {
        fun fromData(unit: DataDateFormat) = when (unit) {
            DataDateFormat.REVERSED -> REVERSED
            DataDateFormat.STRAIGHT -> STRAIGHT
        }
    }
}