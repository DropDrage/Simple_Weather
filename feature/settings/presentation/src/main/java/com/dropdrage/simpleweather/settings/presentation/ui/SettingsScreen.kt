package com.dropdrage.simpleweather.settings.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dropdrage.simpleweather.core.presentation.ui.TextWithSubtext
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Small150
import com.dropdrage.simpleweather.settings.presentation.SettingsViewModel
import com.dropdrage.simpleweather.settings.presentation.model.ViewSetting

@Composable
fun SettingsScreen() {
    val viewModel = hiltViewModel<SettingsViewModel>()

    var editSetting by remember { mutableStateOf<ViewSetting?>(null) }

    fun onSettingClick(setting: ViewSetting) {
        editSetting = setting
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.settings) {
            SettingItem(
                setting = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingClick(it) }
                    .padding(horizontal = Medium100, vertical = Small150)
            )
        }
    }
    if (editSetting != null) {
        SettingChangeDialog(
            viewSetting = editSetting!!,
            selectedSetting = viewModel.getCurrentValue(editSetting!!.values[0]),
            onDismissRequest = { editSetting = null },
            onSettingChanged = viewModel::changeSetting
        )
    }
}

@Composable
private fun SettingItem(setting: ViewSetting, modifier: Modifier = Modifier) {
    TextWithSubtext(
        text = setting.label,
        textStyle = MaterialTheme.typography.bodyLarge,
        subtext = setting.currentValue,
        subtextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsItemPreview() {
    ComposeMaterial3Theme {
        SettingItem(
            setting = ViewSetting("Temperature", "C", emptyList()),
            modifier = Modifier.fillMaxWidth()
        )
    }
}