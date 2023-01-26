package com.dropdrage.common.presentation.ui

import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.dropdrage.common.presentation.utils.TitledAppBar

abstract class TitledFragment(@LayoutRes layoutResId: Int, @StringRes private val titleResId: Int) :
    Fragment(layoutResId) {
    @CallSuper
    override fun onResume() {
        (activity as TitledAppBar).setTitle(getString(titleResId))
        super.onResume()
    }
}
