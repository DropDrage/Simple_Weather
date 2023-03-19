package com.dropdrage.test.util

import android.os.Looper
import android.util.Log
import io.mockk.MockKMatcherScope
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
        justMock { Looper.getMainLooper() }

        block()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLooper(crossinline block: suspend () -> Unit) = runTest {
    mockkStatic(Looper::class) {
        justMock { Looper.getMainLooper() }

        block()
    }
}


inline fun <reified T : Any> justMock(noinline stubBlock: MockKMatcherScope.() -> T) {
    every(stubBlock) returns mockk()
}

inline fun <reified T : Any> justMock(noinline stubBlock: MockKMatcherScope.() -> T, mockInit: T.() -> Unit = {}) {
    every(stubBlock) returns mockk(block = mockInit)
}

inline fun <reified T : Any> justMockAndGet(noinline stubBlock: MockKMatcherScope.() -> T): T {
    val mock = mockk<T>()
    every(stubBlock) returns mock
    return mock
}
