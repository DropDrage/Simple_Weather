package com.dropdrage.simpleweather.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.dropdrage.simpleweather.core.presentation.model.ViewWeatherType

@Composable
fun WeatherWithTemperature(
    weather: ViewWeatherType,
    temperature: String,
    iconSize: Dp,
    spacing: Dp,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = weather.iconRes),
            contentDescription = stringResource(id = weather.weatherDescriptionRes),
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.height(spacing))
        Text(text = temperature, style = textStyle)
    }
}
