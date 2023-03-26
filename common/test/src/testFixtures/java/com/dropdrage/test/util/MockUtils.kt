package com.dropdrage.test.util

import android.os.Looper
import android.util.Log
import io.mockk.MockKMatcherScope
import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun mockLogD(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    mockkStatic(Log::class) {
        every { Log.d(any<String>(), any<String>()) } returns 0
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

@OptIn(ExperimentalContracts::class)
inline fun mockLogEShort(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    mockkStatic(Log::class) {
        every { Log.e(any(), any()) } returns 0
        block()
    }
}

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


@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLogD(crossinline block: suspend () -> Unit) = runTest {
    mockLogD { block() }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLogEShort(crossinline block: suspend () -> Unit) = runTest {
    mockLogEShort { block() }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestWithMockLogE(crossinline block: suspend () -> Unit) = runTest {
    mockLogE { block() }
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


fun verifyNever(block: MockKVerificationScope.() -> Unit) = verify(exactly = 0, verifyBlock = block)

fun verifyOnce(block: MockKVerificationScope.() -> Unit) = verify(exactly = 1, verifyBlock = block)

fun verifyTwice(block: MockKVerificationScope.() -> Unit) = verify(exactly = 2, verifyBlock = block)


fun coVerifyNever(block: suspend MockKVerificationScope.() -> Unit) = coVerify(exactly = 0, verifyBlock = block)

fun coVerifyOnce(block: suspend MockKVerificationScope.() -> Unit) = coVerify(exactly = 1, verifyBlock = block)

fun coVerifyTwice(block: suspend MockKVerificationScope.() -> Unit) = coVerify(exactly = 2, verifyBlock = block)
