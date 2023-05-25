package com.dropdrage.common.build_config_checks

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(parameter = 0)
fun isSdkVersionGreaterOrEquals(version: Int) = Build.VERSION.SDK_INT >= version
