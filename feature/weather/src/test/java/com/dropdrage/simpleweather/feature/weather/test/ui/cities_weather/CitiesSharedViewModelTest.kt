package com.dropdrage.simpleweather.feature.weather.test.ui.cities_weather

import app.cash.turbine.test
import com.dropdrage.common.presentation.util.TextMessage
import com.dropdrage.common.test.util.runTestViewModelScope
import com.dropdrage.simpleweather.feature.weather.presentation.model.ViewCityTitle
import com.dropdrage.simpleweather.feature.weather.presentation.ui.cities_weather.CitiesSharedViewModel
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CitiesSharedViewModelTest {

    private val viewModel = CitiesSharedViewModel()

    @Test
    fun `setCityTitle success`() = runTestViewModelScope {
        val expectedTitle = ViewCityTitle(mockk<TextMessage>(), mockk())

        viewModel.currentCityTitle.test {
            viewModel.setCityTitle(expectedTitle)
            val newTitle = awaitItem()

            cancel()

            Assertions.assertEquals(expectedTitle, newTitle)
        }
    }

}
