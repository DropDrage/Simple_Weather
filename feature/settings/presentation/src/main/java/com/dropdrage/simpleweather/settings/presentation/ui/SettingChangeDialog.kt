package com.dropdrage.simpleweather.settings.presentation.ui

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.dropdrage.simpleweather.core.style.ComposeMaterial3Theme
import com.dropdrage.simpleweather.core.style.Medium100
import com.dropdrage.simpleweather.core.style.Medium150
import com.dropdrage.simpleweather.core.style.Small100
import com.dropdrage.simpleweather.settings.WindSpeedUnit
import com.dropdrage.simpleweather.settings.presentation.R
import com.dropdrage.simpleweather.settings.presentation.model.AnySetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewSetting
import com.dropdrage.simpleweather.settings.presentation.model.ViewTemperatureUnit
import com.dropdrage.simpleweather.settings.presentation.model.ViewWindSpeedUnit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SettingChangeDialog(
    viewSetting: ViewSetting,
    selectedSetting: AnySetting,
    onDismissRequest: () -> Unit,
    onSettingChanged: (AnySetting) -> Unit,
) {
    fun onSettingClick(setting: AnySetting) {
        onSettingChanged(setting)
        onDismissRequest()
    }

    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Medium100, end = Medium100, bottom = Medium150)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(Medium100))
                    .padding(vertical = Medium100)
            ) {
                Text(
                    text = viewSetting.label,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(Medium150))
                Column(modifier = Modifier.fillMaxWidth()) {
                    viewSetting.values.forEach { setting ->
                        SettingChoiceItem(
                            setting = setting,
                            isSelected = selectedSetting === setting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSettingClick(setting) }
                                .padding(vertical = Small100, horizontal = Medium100)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Medium150))
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        }
    }
}

@Composable
private fun SettingChoiceItem(
    setting: AnySetting,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = setting.unitResId, ""),
            style = MaterialTheme.typography.bodyLarge
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(Medium150))
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 300)
@Composable
private fun SettingChoiceItemPreview() {
    ComposeMaterial3Theme {
        Column {
            SettingChoiceItem(
                setting = ViewTemperatureUnit.CELSIUS,
                isSelected = true,
            )
            SettingChoiceItem(
                setting = ViewTemperatureUnit.CELSIUS,
                isSelected = false,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 500, device = Devices.PHONE)
@Composable
private fun SettingChangeDialogPreview() {
    ComposeMaterial3Theme {
        SettingChangeDialog(
            ViewSetting(
                "Wind Speed",
                "m/s",
                ViewWindSpeedUnit.fromData(WindSpeedUnit.M_S).values
            ),
            ViewWindSpeedUnit.M_S,
            {},
            {}
        )
    }
}
