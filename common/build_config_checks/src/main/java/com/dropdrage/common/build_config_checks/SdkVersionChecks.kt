package com.dropdrage.common.build_config_checks

import android.os.Build

fun isSdkVersionGreaterOrEquals(version: Int) = Build.VERSION.SDK_INT >= version
