package com.dropdrage.test.util

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

inline fun mockLogE(block: () -> Unit) = mockkStatic(Log::class) {
    every { Log.e(any(), any(), any()) } returns 0
    block()
}

inline fun mockLogW(block: () -> Unit) = mockkStatic(Log::class) {
    every { Log.w(any<String>(), any<String>()) } returns 0
    block()
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLogE(crossinline block: suspend () -> Unit) = runTest {
    mockkStatic(Log::class) {
        every { Log.e(any(), any(), any()) } returns 0
        block()
    }
}

