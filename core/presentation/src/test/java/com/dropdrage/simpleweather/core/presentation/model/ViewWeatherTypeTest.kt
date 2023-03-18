package com.dropdrage.simpleweather.core.presentation.model

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import org.junit.Assert.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ViewWeatherTypeTest {

    @ParameterizedTest
    @MethodSource("provideWeatherTypeWithView")
    fun `ViewWeatherType from weatherType`(weatherType: WeatherType, viewWeatherType: ViewWeatherType) {
        val result = ViewWeatherType.fromWeatherType(weatherType)

        assertEquals(viewWeatherType, result)
    }


    companion object {
        @JvmStatic
        fun provideWeatherTypeWithView() = Stream.of(
            Arguments.of(WeatherType.ClearSky, ViewWeatherType.ClearSky),
            Arguments.of(WeatherType.MainlyClear, ViewWeatherType.MainlyClear),
            Arguments.of(WeatherType.PartlyCloudy, ViewWeatherType.PartlyCloudy),
            Arguments.of(WeatherType.Overcast, ViewWeatherType.Overcast),
            Arguments.of(WeatherType.Foggy, ViewWeatherType.Foggy),
            Arguments.of(WeatherType.DepositingRimeFog, ViewWeatherType.DepositingRimeFog),
            Arguments.of(WeatherType.LightDrizzle, ViewWeatherType.LightDrizzle),
            Arguments.of(WeatherType.ModerateDrizzle, ViewWeatherType.ModerateDrizzle),
            Arguments.of(WeatherType.DenseDrizzle, ViewWeatherType.DenseDrizzle),
            Arguments.of(WeatherType.LightFreezingDrizzle, ViewWeatherType.LightFreezingDrizzle),
            Arguments.of(WeatherType.DenseFreezingDrizzle, ViewWeatherType.DenseFreezingDrizzle),
            Arguments.of(WeatherType.SlightRain, ViewWeatherType.SlightRain),
            Arguments.of(WeatherType.ModerateRain, ViewWeatherType.ModerateRain),
            Arguments.of(WeatherType.HeavyRain, ViewWeatherType.HeavyRain),
            Arguments.of(WeatherType.HeavyFreezingRain, ViewWeatherType.HeavyFreezingRain),
            Arguments.of(WeatherType.SlightSnowFall, ViewWeatherType.SlightSnowFall),
            Arguments.of(WeatherType.ModerateSnowFall, ViewWeatherType.ModerateSnowFall),
            Arguments.of(WeatherType.HeavySnowFall, ViewWeatherType.HeavySnowFall),
            Arguments.of(WeatherType.SnowGrains, ViewWeatherType.SnowGrains),
            Arguments.of(WeatherType.SlightRainShowers, ViewWeatherType.SlightRainShowers),
            Arguments.of(WeatherType.ModerateRainShowers, ViewWeatherType.ModerateRainShowers),
            Arguments.of(WeatherType.ViolentRainShowers, ViewWeatherType.ViolentRainShowers),
            Arguments.of(WeatherType.SlightSnowShowers, ViewWeatherType.SlightSnowShowers),
            Arguments.of(WeatherType.HeavySnowShowers, ViewWeatherType.HeavySnowShowers),
            Arguments.of(WeatherType.ModerateThunderstorm, ViewWeatherType.ModerateThunderstorm),
            Arguments.of(WeatherType.SlightHailThunderstorm, ViewWeatherType.SlightHailThunderstorm),
            Arguments.of(WeatherType.HeavyHailThunderstorm, ViewWeatherType.HeavyHailThunderstorm),
        )
    }

}
