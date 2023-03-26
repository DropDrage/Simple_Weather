package com.dropdrage.simpleweather.data.weather.test.utils

import com.dropdrage.simpleweather.core.domain.weather.WeatherType
import com.dropdrage.simpleweather.data.weather.utils.WeatherTypeConverter
import com.dropdrage.test.util.mockLogW
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class WeatherTypeConverterTest {

    @ParameterizedTest
    @MethodSource("provideWeatherCodeWithType")
    fun `toWeatherType success`(weatherCode: Int, weatherType: WeatherType) {
        val resultWeatherType = WeatherTypeConverter.toWeatherType(weatherCode)

        assertEquals(weatherType, resultWeatherType)
    }

    @Test
    fun `toWeatherType unsupported code then ClearSky`() = mockLogW {
        val resultWeatherType = WeatherTypeConverter.toWeatherType(-100)

        assertEquals(WeatherType.ClearSky, resultWeatherType)
    }


    @ParameterizedTest
    @MethodSource("provideWeatherTypeWithCode")
    fun `fromWeatherType success`(weatherType: WeatherType, weatherCode: Int) {
        val resultWeatherCode = WeatherTypeConverter.fromWeatherType(weatherType)

        assertEquals(weatherCode, resultWeatherCode)
    }


    companion object {
        @JvmStatic
        fun provideWeatherCodeWithType() = Stream.of(
            Arguments.of(0, WeatherType.ClearSky),
            Arguments.of(1, WeatherType.MainlyClear),
            Arguments.of(2, WeatherType.PartlyCloudy),
            Arguments.of(3, WeatherType.Overcast),
            Arguments.of(45, WeatherType.Foggy),
            Arguments.of(48, WeatherType.DepositingRimeFog),
            Arguments.of(51, WeatherType.LightDrizzle),
            Arguments.of(53, WeatherType.ModerateDrizzle),
            Arguments.of(55, WeatherType.DenseDrizzle),
            Arguments.of(56, WeatherType.LightFreezingDrizzle),
            Arguments.of(66, WeatherType.LightFreezingDrizzle),
            Arguments.of(57, WeatherType.DenseFreezingDrizzle),
            Arguments.of(61, WeatherType.SlightRain),
            Arguments.of(63, WeatherType.ModerateRain),
            Arguments.of(65, WeatherType.HeavyRain),
            Arguments.of(67, WeatherType.HeavyFreezingRain),
            Arguments.of(71, WeatherType.SlightSnowFall),
            Arguments.of(73, WeatherType.ModerateSnowFall),
            Arguments.of(75, WeatherType.HeavySnowFall),
            Arguments.of(77, WeatherType.SnowGrains),
            Arguments.of(80, WeatherType.SlightRainShowers),
            Arguments.of(81, WeatherType.ModerateRainShowers),
            Arguments.of(82, WeatherType.ViolentRainShowers),
            Arguments.of(85, WeatherType.SlightSnowShowers),
            Arguments.of(86, WeatherType.HeavySnowShowers),
            Arguments.of(95, WeatherType.ModerateThunderstorm),
            Arguments.of(96, WeatherType.SlightHailThunderstorm),
            Arguments.of(99, WeatherType.HeavyHailThunderstorm),
        )

        @JvmStatic
        fun provideWeatherTypeWithCode() = Stream.of(
            Arguments.of(WeatherType.ClearSky, 0),
            Arguments.of(WeatherType.MainlyClear, 1),
            Arguments.of(WeatherType.PartlyCloudy, 2),
            Arguments.of(WeatherType.Overcast, 3),
            Arguments.of(WeatherType.Foggy, 45),
            Arguments.of(WeatherType.DepositingRimeFog, 48),
            Arguments.of(WeatherType.LightDrizzle, 51),
            Arguments.of(WeatherType.ModerateDrizzle, 53),
            Arguments.of(WeatherType.DenseDrizzle, 55),
            Arguments.of(WeatherType.LightFreezingDrizzle, 56),
            Arguments.of(WeatherType.DenseFreezingDrizzle, 57),
            Arguments.of(WeatherType.SlightRain, 61),
            Arguments.of(WeatherType.ModerateRain, 63),
            Arguments.of(WeatherType.HeavyRain, 65),
            Arguments.of(WeatherType.HeavyFreezingRain, 67),
            Arguments.of(WeatherType.SlightSnowFall, 71),
            Arguments.of(WeatherType.ModerateSnowFall, 73),
            Arguments.of(WeatherType.HeavySnowFall, 75),
            Arguments.of(WeatherType.SnowGrains, 77),
            Arguments.of(WeatherType.SlightRainShowers, 80),
            Arguments.of(WeatherType.ModerateRainShowers, 81),
            Arguments.of(WeatherType.ViolentRainShowers, 82),
            Arguments.of(WeatherType.SlightSnowShowers, 85),
            Arguments.of(WeatherType.HeavySnowShowers, 86),
            Arguments.of(WeatherType.ModerateThunderstorm, 95),
            Arguments.of(WeatherType.SlightHailThunderstorm, 96),
            Arguments.of(WeatherType.HeavyHailThunderstorm, 99),
        )
    }

}
