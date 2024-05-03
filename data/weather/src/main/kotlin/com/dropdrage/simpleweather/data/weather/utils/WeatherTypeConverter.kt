package com.dropdrage.simpleweather.data.weather.utils

import android.util.Log
import androidx.room.TypeConverter
import com.dropdrage.simpleweather.core.domain.weather.WeatherType

internal object WeatherTypeConverter {

    private const val TAG = "WeatherTypeConverter"

    //region Int constants

    private const val CLEAR_SKY = 0
    private const val MAINLY_CLEAR = 1
    private const val PARTLY_CLOUDY = 2
    private const val OVERCAST = 3
    private const val FOGGY = 45
    private const val DEPOSITING_RIME_FOG = 48
    private const val LIGHT_DRIZZLE = 51
    private const val MODERATE_DRIZZLE = 53
    private const val DENSE_DRIZZLE = 55
    private const val LIGHT_FREEZING_DRIZZLE = 56
    private const val LIGHT_FREEZING_DRIZZLE_2 = 66
    private const val DENSE_FREEZING_DRIZZLE = 57
    private const val SLIGHT_RAIN = 61
    private const val MODERATE_RAIN = 63
    private const val HEAVY_RAIN = 65
    private const val HEAVY_FREEZING_RAIN = 67
    private const val SLIGHT_SNOW_FALL = 71
    private const val MODERATE_SNOW_FALL = 73
    private const val HEAVY_SNOW_FALL = 75
    private const val SNOW_GRAINS = 77
    private const val SLIGHT_RAIN_SHOWERS = 80
    private const val MODERATE_RAIN_SHOWERS = 81
    private const val VIOLENT_RAIN_SHOWERS = 82
    private const val SLIGHT_SNOW_SHOWERS = 85
    private const val HEAVY_SNOW_SHOWERS = 86
    private const val MODERATE_THUNDERSTORM = 95
    private const val SLIGHT_HAIL_THUNDERSTORM = 96
    private const val HEAVY_HAIL_THUNDERSTORM = 99

    //endregion


    @TypeConverter
    fun toWeatherType(code: Int): WeatherType = when (code) {
        CLEAR_SKY -> WeatherType.ClearSky
        MAINLY_CLEAR -> WeatherType.MainlyClear
        PARTLY_CLOUDY -> WeatherType.PartlyCloudy
        OVERCAST -> WeatherType.Overcast
        FOGGY -> WeatherType.Foggy
        DEPOSITING_RIME_FOG -> WeatherType.DepositingRimeFog
        LIGHT_DRIZZLE -> WeatherType.LightDrizzle
        MODERATE_DRIZZLE -> WeatherType.ModerateDrizzle
        DENSE_DRIZZLE -> WeatherType.DenseDrizzle
        LIGHT_FREEZING_DRIZZLE, LIGHT_FREEZING_DRIZZLE_2 -> WeatherType.LightFreezingDrizzle
        DENSE_FREEZING_DRIZZLE -> WeatherType.DenseFreezingDrizzle
        SLIGHT_RAIN -> WeatherType.SlightRain
        MODERATE_RAIN -> WeatherType.ModerateRain
        HEAVY_RAIN -> WeatherType.HeavyRain
        HEAVY_FREEZING_RAIN -> WeatherType.HeavyFreezingRain
        SLIGHT_SNOW_FALL -> WeatherType.SlightSnowFall
        MODERATE_SNOW_FALL -> WeatherType.ModerateSnowFall
        HEAVY_SNOW_FALL -> WeatherType.HeavySnowFall
        SNOW_GRAINS -> WeatherType.SnowGrains
        SLIGHT_RAIN_SHOWERS -> WeatherType.SlightRainShowers
        MODERATE_RAIN_SHOWERS -> WeatherType.ModerateRainShowers
        VIOLENT_RAIN_SHOWERS -> WeatherType.ViolentRainShowers
        SLIGHT_SNOW_SHOWERS -> WeatherType.SlightSnowShowers
        HEAVY_SNOW_SHOWERS -> WeatherType.HeavySnowShowers
        MODERATE_THUNDERSTORM -> WeatherType.ModerateThunderstorm
        SLIGHT_HAIL_THUNDERSTORM -> WeatherType.SlightHailThunderstorm
        HEAVY_HAIL_THUNDERSTORM -> WeatherType.HeavyHailThunderstorm
        else -> {
            Log.w(TAG, "Unsupported code: $code")
            WeatherType.ClearSky
        }
    }

    @TypeConverter
    fun fromWeatherType(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.ClearSky -> CLEAR_SKY
        WeatherType.MainlyClear -> MAINLY_CLEAR
        WeatherType.PartlyCloudy -> PARTLY_CLOUDY
        WeatherType.Overcast -> OVERCAST
        WeatherType.Foggy -> FOGGY
        WeatherType.DepositingRimeFog -> DEPOSITING_RIME_FOG
        WeatherType.LightDrizzle -> LIGHT_DRIZZLE
        WeatherType.ModerateDrizzle -> MODERATE_DRIZZLE
        WeatherType.DenseDrizzle -> DENSE_DRIZZLE
        WeatherType.LightFreezingDrizzle -> LIGHT_FREEZING_DRIZZLE
        WeatherType.DenseFreezingDrizzle -> DENSE_FREEZING_DRIZZLE
        WeatherType.SlightRain -> SLIGHT_RAIN
        WeatherType.ModerateRain -> MODERATE_RAIN
        WeatherType.HeavyRain -> HEAVY_RAIN
        WeatherType.HeavyFreezingRain -> HEAVY_FREEZING_RAIN
        WeatherType.SlightSnowFall -> SLIGHT_SNOW_FALL
        WeatherType.ModerateSnowFall -> MODERATE_SNOW_FALL
        WeatherType.HeavySnowFall -> HEAVY_SNOW_FALL
        WeatherType.SnowGrains -> SNOW_GRAINS
        WeatherType.SlightRainShowers -> SLIGHT_RAIN_SHOWERS
        WeatherType.ModerateRainShowers -> MODERATE_RAIN_SHOWERS
        WeatherType.ViolentRainShowers -> VIOLENT_RAIN_SHOWERS
        WeatherType.SlightSnowShowers -> SLIGHT_SNOW_SHOWERS
        WeatherType.HeavySnowShowers -> HEAVY_SNOW_SHOWERS
        WeatherType.ModerateThunderstorm -> MODERATE_THUNDERSTORM
        WeatherType.SlightHailThunderstorm -> SLIGHT_HAIL_THUNDERSTORM
        WeatherType.HeavyHailThunderstorm -> HEAVY_HAIL_THUNDERSTORM
    }

}
