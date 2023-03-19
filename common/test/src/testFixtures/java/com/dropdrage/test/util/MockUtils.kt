package com.dropdrage.test.util

import android.os.Looper
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun mockLogE(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    mockkStatic(Log::class) {
        every { Log.e(any(), any(), any()) } returns 0
        block()
    }
}

@OptIn(ExperimentalContracts::class)
inline fun mockLogW(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    mockkStatic(Log::class) {
        every { Log.w(any<String>(), any<String>()) } returns 0
        block()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLogE(crossinline block: suspend () -> Unit) = runTest {
    mockkStatic(Log::class) {
        every { Log.e(any(), any(), any()) } returns 0
        block()
    }
}


@OptIn(ExperimentalContracts::class)
inline fun mockLooper(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    mockkStatic(Looper::class) {
        every { Looper.getMainLooper() } returns mockk()

        block()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLooper(crossinline block: suspend () -> Unit) = runTest {
    mockkStatic(Looper::class) {
        every { Looper.getMainLooper() } returns mockk()

        block()
    }
}
