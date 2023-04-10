package com.dropdrage.simpleweather.weather.presentation.test.utils.extension

import android.content.Context
import android.widget.ImageView
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType
import com.dropdrage.simpleweather.weather.presentation.util.extension.setWeather
import com.dropdrage.test.util.justArgToString
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ImageViewExtensionsTest {

    @ParameterizedTest
    @MethodSource("provideViewWeatherType")
    fun `ImageView setWeather success`(weatherType: ViewWeatherType) {
        val context = mockk<Context> {
            justArgToString { getString(eq(weatherType.weatherDescriptionRes)) }
        }
        val imageRes = slot<Int>()
        val contentDescription = slot<String>()
        val imageView = mockk<ImageView> {
            every { this@mockk.context } returns context
            justRun { setImageResource(capture(imageRes)) }
            justRun { this@mockk.contentDescription = capture(contentDescription) }
        }

        imageView.setWeather(weatherType)

        assertEquals(weatherType.iconRes, imageRes.captured)
        assertEquals(weatherType.weatherDescriptionRes.toString(), contentDescription.captured)
    }


    private companion object {
        @JvmStatic
        fun provideViewWeatherType() = Stream.of(
            ViewWeatherType.ClearSky,
            ViewWeatherType.MainlyClear,
            ViewWeatherType.PartlyCloudy,
            ViewWeatherType.Overcast,
            ViewWeatherType.Foggy,
            ViewWeatherType.DepositingRimeFog,
            ViewWeatherType.LightDrizzle,
            ViewWeatherType.ModerateDrizzle,
            ViewWeatherType.DenseDrizzle,
            ViewWeatherType.LightFreezingDrizzle,
            ViewWeatherType.DenseFreezingDrizzle,
            ViewWeatherType.SlightRain,
            ViewWeatherType.ModerateRain,
            ViewWeatherType.HeavyRain,
            ViewWeatherType.HeavyFreezingRain,
            ViewWeatherType.SlightSnowFall,
            ViewWeatherType.ModerateSnowFall,
            ViewWeatherType.HeavySnowFall,
            ViewWeatherType.SnowGrains,
            ViewWeatherType.SlightRainShowers,
            ViewWeatherType.ModerateRainShowers,
            ViewWeatherType.ViolentRainShowers,
            ViewWeatherType.SlightSnowShowers,
            ViewWeatherType.HeavySnowShowers,
            ViewWeatherType.ModerateThunderstorm,
            ViewWeatherType.SlightHailThunderstorm,
            ViewWeatherType.HeavyHailThunderstorm,
        )
    }

}
