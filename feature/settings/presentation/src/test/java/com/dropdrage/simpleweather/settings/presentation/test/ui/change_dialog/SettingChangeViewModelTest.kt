package com.dropdrage.simpleweather.settings.presentation.test.ui.change_dialog

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.dropdrage.simpleweather.settings.presentation.change_dialog.SettingChangeViewModel
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.test.util.runTestViewModelScope
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingChangeViewModelTest {

    private val viewModel = SettingChangeViewModel()


    @Test
    fun `setSelectedSetting returns same`() = runTestViewModelScope {
        viewModel.selectedSetting.test {
            val setting = mockk<AnySetting>()
            viewModel.setSelectedSetting(setting)

            assertSame(setting, awaitItem())
        }
    }

    @Test
    fun `changeTargetSetting returns title and empty list`() = runTestViewModelScope {
        val title = "Title"
        val values = emptyList<AnySetting>()
        val titleFlow = viewModel.title.testIn(backgroundScope)
        val valuesFlow = viewModel.values.testIn(backgroundScope)

        viewModel.changeTargetSetting(title, values)
        val defaultTitle = titleFlow.awaitItem()
        val changedTitle = titleFlow.awaitItem()
        val defaultValues = valuesFlow.awaitItem()
        val restValues = valuesFlow.cancelAndConsumeRemainingEvents()

        assertThat(defaultTitle).isEmpty()
        assertEquals(title, changedTitle)
        assertThat(defaultValues).isEmpty()
        assertThat(restValues).isEmpty()
    }

    @Test
    fun `changeTargetSetting returns title and filled list`() = runTestViewModelScope {
        val title = "Title"
        val values = ViewTemperatureUnit.CELSIUS.values
        val titleFlow = viewModel.title.testIn(backgroundScope)
        val valuesFlow = viewModel.values.testIn(backgroundScope)

        viewModel.changeTargetSetting(title, values)
        viewModel.changeTargetSetting(title, values)
        val defaultTitle = titleFlow.awaitItem()
        val changedTitle = titleFlow.awaitItem()
        val defaultValues = valuesFlow.awaitItem()
        val changedValues = valuesFlow.awaitItem()

        assertThat(defaultTitle).isEmpty()
        assertEquals(title, changedTitle)
        assertThat(defaultValues).isEmpty()
        assertThat(changedValues).containsExactlyElementsIn(values)
    }

}
