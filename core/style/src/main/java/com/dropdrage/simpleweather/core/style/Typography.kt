package com.dropdrage.simpleweather.core.style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

fun MyTypography(colors: ColorScheme): Typography = Typography(
    headlineLarge = TextStyle(
        color = colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = Font32,
    ),

    titleLarge = TextStyle(
        color = colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = Font20,
    ),

    bodySmall = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = Font12,
    ),
    bodyMedium = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = Font14,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = Font16,
    ),

    labelMedium = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = Font12
    )
)
