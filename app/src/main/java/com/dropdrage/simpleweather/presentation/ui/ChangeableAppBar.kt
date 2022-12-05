package com.dropdrage.simpleweather.presentation.ui

import androidx.appcompat.widget.Toolbar

interface ChangeableAppBar {
    fun changeAppBar(toolbar: Toolbar)

    fun restoreDefaultAppBar()
}