package com.dropdrage.simpleweather.feature.weather.presentation.util

internal object ViewUtils {
    fun resizeToTargetSize(iconIntrinsicWidth: Int, iconIntrinsicHeight: Int, iconSize: Int): Pair<Int, Int> {
        val scaleFactor: Float =
            if (iconIntrinsicWidth > iconIntrinsicHeight) iconSize / (iconIntrinsicWidth).toFloat()
            else iconSize / (iconIntrinsicHeight).toFloat()

        val width: Int = (iconIntrinsicWidth * scaleFactor).toInt()
        val height: Int = (iconIntrinsicHeight * scaleFactor).toInt()

        return width to height
    }
}