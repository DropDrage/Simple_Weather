package com.dropdrage.simpleweather.core.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.core.presentation.R

sealed class ViewWeatherType(
    @StringRes val weatherDescriptionRes: Int,
    @DrawableRes val iconRes: Int,
) {
    object ClearSky : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_clear_sky,
        iconRes = R.drawable.ic_sunny
    )

    object MainlyClear : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_mainly_clear,
        iconRes = R.drawable.ic_cloudy
    )

    object PartlyCloudy : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_partly_cloudy,
        iconRes = R.drawable.ic_cloudy
    )

    object Overcast : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_overcast,
        iconRes = R.drawable.ic_cloudy
    )

    object Foggy : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_foggy,
        iconRes = R.drawable.ic_very_cloudy
    )

    object DepositingRimeFog : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_depositing_rime_fog,
        iconRes = R.drawable.ic_very_cloudy
    )

    object LightDrizzle : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_light_drizzle,
        iconRes = R.drawable.ic_rainshower
    )

    object ModerateDrizzle : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_moderate_drizzle,
        iconRes = R.drawable.ic_rainshower
    )

    object DenseDrizzle : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_dense_drizzle,
        iconRes = R.drawable.ic_rainshower
    )

    object LightFreezingDrizzle : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_slight_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )

    object DenseFreezingDrizzle : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_dense_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )

    object SlightRain : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_slight_rain,
        iconRes = R.drawable.ic_rainy
    )

    object ModerateRain : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_rainy,
        iconRes = R.drawable.ic_rainy
    )

    object HeavyRain : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_heavy_rain,
        iconRes = R.drawable.ic_rainy
    )

    object HeavyFreezingRain : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_heavy_freezing_rain,
        iconRes = R.drawable.ic_snowyrainy
    )

    object SlightSnowFall : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_slight_snow_fall,
        iconRes = R.drawable.ic_snowy
    )

    object ModerateSnowFall : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_moderate_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )

    object HeavySnowFall : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_heavy_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )

    object SnowGrains : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_snow_grains,
        iconRes = R.drawable.ic_heavysnow
    )

    object SlightRainShowers : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_slight_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )

    object ModerateRainShowers : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_moderate_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )

    object ViolentRainShowers : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_violent_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )

    object SlightSnowShowers : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_light_snow_showers,
        iconRes = R.drawable.ic_snowy
    )

    object HeavySnowShowers : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_heavy_snow_showers,
        iconRes = R.drawable.ic_snowy
    )

    object ModerateThunderstorm : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_moderate_thunderstorm,
        iconRes = R.drawable.ic_thunder
    )

    object SlightHailThunderstorm : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_thunderstorm_with_slight_hail,
        iconRes = R.drawable.ic_rainythunder
    )

    object HeavyHailThunderstorm : ViewWeatherType(
        weatherDescriptionRes = R.string.weather_label_thunderstorm_with_heavy_hail,
        iconRes = R.drawable.ic_rainythunder
    )

    companion object {
        fun fromWeatherType(weatherType: WeatherType): ViewWeatherType = when (weatherType) {
            WeatherType.ClearSky -> ClearSky
            WeatherType.MainlyClear -> MainlyClear
            WeatherType.PartlyCloudy -> PartlyCloudy
            WeatherType.Overcast -> Overcast
            WeatherType.Foggy -> Foggy
            WeatherType.DepositingRimeFog -> DepositingRimeFog
            WeatherType.LightDrizzle -> LightDrizzle
            WeatherType.ModerateDrizzle -> ModerateDrizzle
            WeatherType.DenseDrizzle -> DenseDrizzle
            WeatherType.LightFreezingDrizzle -> LightFreezingDrizzle
            WeatherType.DenseFreezingDrizzle -> DenseFreezingDrizzle
            WeatherType.SlightRain -> SlightRain
            WeatherType.ModerateRain -> ModerateRain
            WeatherType.HeavyRain -> HeavyRain
            WeatherType.HeavyFreezingRain -> HeavyFreezingRain
            WeatherType.SlightSnowFall -> SlightSnowFall
            WeatherType.ModerateSnowFall -> ModerateSnowFall
            WeatherType.HeavySnowFall -> HeavySnowFall
            WeatherType.SnowGrains -> SnowGrains
            WeatherType.SlightRainShowers -> SlightRainShowers
            WeatherType.ModerateRainShowers -> ModerateRainShowers
            WeatherType.ViolentRainShowers -> ViolentRainShowers
            WeatherType.SlightSnowShowers -> SlightSnowShowers
            WeatherType.HeavySnowShowers -> HeavySnowShowers
            WeatherType.ModerateThunderstorm -> ModerateThunderstorm
            WeatherType.SlightHailThunderstorm -> SlightHailThunderstorm
            WeatherType.HeavyHailThunderstorm -> HeavyHailThunderstorm
        }
    }
}