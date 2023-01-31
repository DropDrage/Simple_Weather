package com.dropdrage.common.presentation.utils

import androidx.appcompat.widget.Toolbar

interface ChangeableAppBar {
    fun changeAppBar(toolbar: Toolbar)

    fun hideAppBar()

    fun restoreDefaultAppBar()
}