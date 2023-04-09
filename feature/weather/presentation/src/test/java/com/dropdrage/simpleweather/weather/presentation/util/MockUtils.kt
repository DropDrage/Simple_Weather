package com.dropdrage.simpleweather.weather.presentation.util

import com.dropdrage.simpleweather.weather.domain.weather.DayWeather
import com.dropdrage.simpleweather.weather.domain.weather.HourWeather
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentDayWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.CurrentHourWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.DailyWeatherConverter
import com.dropdrage.simpleweather.weather.presentation.util.model_converter.HourWeatherConverter
import io.mockk.every

internal fun mockConverters(
    currentHourWeatherConverter: CurrentHourWeatherConverter,
    currentDayWeatherConverter: CurrentDayWeatherConverter,
    hourWeatherConverter: HourWeatherConverter,
    dailyWeatherConverter: DailyWeatherConverter,
) {
    every { currentDayWeatherConverter.convertToView(any()) } answers {
        val dayWeather = firstArg<DayWeather>()
        toViewCurrentDayWeather(dayWeather)
    }
    every { currentHourWeatherConverter.convertToView(any()) } answers {
        val hourWeather = firstArg<HourWeather>()
        toViewCurrentHourWeather(hourWeather)
    }

    every { dailyWeatherConverter.convertToView(any(), any()) } answers {
        val dayWeather = firstArg<DayWeather>()
        toViewDayWeather(dayWeather)
    }
    every { hourWeatherConverter.convertToView(any(), any()) } answers {
        val hourWeather = firstArg<HourWeather>()
        toViewHourWeather(hourWeather)
    }
}
