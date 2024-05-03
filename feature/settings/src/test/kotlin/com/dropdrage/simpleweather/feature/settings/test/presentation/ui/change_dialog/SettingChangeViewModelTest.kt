package com.dropdrage.simpleweather.feature.settings.test.presentation.ui.change_dialog

import app.cash.turbine.test
import com.dropdrage.common.test.util.ViewModelScopeExtension
import com.dropdrage.common.test.util.runTurbineTest
import com.dropdrage.simpleweather.feature.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.feature.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.feature.settings.presentation.ui.change_dialog.SettingChangeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ViewModelScopeExtension::class)
internal class SettingChangeViewModelTest {

    private val viewModel = SettingChangeViewModel()


    @Test
    fun `setSelectedSetting returns same`() = runTest {
        viewModel.selectedSetting.test {
            val setting = mockk<AnySetting>()
            viewModel.setSelectedSetting(setting)

            assertSame(setting, awaitItem())
        }
    }

    @Nested
    inner class changeTargetSetting {

        @Test
        fun `returns title and empty list`() = runTurbineTest { backgroundScope ->
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
        fun `returns title and filled list`() = runTurbineTest { backgroundScope ->
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

}
