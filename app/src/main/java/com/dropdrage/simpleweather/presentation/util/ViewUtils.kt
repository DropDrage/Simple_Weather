package com.dropdrage.simpleweather.presentation.util

object ViewUtils {
    fun resizeToTargetSize(_iconIntrinsicWidth: Int, _iconIntrinsicHeight: Int, iconSize: Int): Pair<Int, Int> {
        val scaleFactor: Float =
            if (_iconIntrinsicWidth > _iconIntrinsicHeight) iconSize / (_iconIntrinsicWidth).toFloat()
            else iconSize / (_iconIntrinsicHeight).toFloat()

        val width: Int = (_iconIntrinsicWidth * scaleFactor).toInt()
        val height: Int = (_iconIntrinsicHeight * scaleFactor).toInt()

        return width to height
    }
}