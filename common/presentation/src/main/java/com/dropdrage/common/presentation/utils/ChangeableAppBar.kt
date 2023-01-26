package com.dropdrage.common.presentation.utils

import androidx.appcompat.widget.Toolbar

interface ChangeableAppBar {
    fun changeAppBar(toolbar: Toolbar, enableHomeButton: Boolean = false)

    fun restoreDefaultAppBar()
}