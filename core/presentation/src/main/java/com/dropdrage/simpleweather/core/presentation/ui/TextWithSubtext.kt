package com.dropdrage.simpleweather.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TextWithSubtext(
    text: String,
    subtext: String,
    textStyle: TextStyle,
    subtextStyle: TextStyle,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            style = textStyle,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = textModifier
        )
        Text(text = subtext, style = subtextStyle)
    }
}
