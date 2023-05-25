package com.dropdrage.common.build_config_checks

import io.mockk.every
import io.mockk.mockkStatic

// ToDo cannot be used bcz testFixtures is not supported for Android Modules
inline fun mockSdkGreaterCheck(isGreater: Boolean, block: () -> Unit) = mockkStatic(::isSdkVersionGreaterOrEquals) {
    every { isSdkVersionGreaterOrEquals(any()) } returns isGreater
    block()
}
