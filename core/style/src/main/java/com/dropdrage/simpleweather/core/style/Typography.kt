package com.dropdrage.simpleweather.core.style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun MyTypography(colors: ColorScheme): Typography = Typography(
    headlineLarge = TextStyle(
        color = colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
    ),

    titleLarge = TextStyle(
        color = colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),

    bodySmall = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    bodyMedium = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),

    labelMedium = TextStyle(
        color = colors.onSurface,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)
