package com.dropdrage.common.test.util

import app.cash.turbine.TurbineContext
import app.cash.turbine.turbineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

inline fun runTestViewModelScopeAndTurbine(
    crossinline block: suspend TurbineContext.(backgroundScope: CoroutineScope) -> Unit,
) = runTestViewModelScope { turbineScope { block(backgroundScope) } }

@OptIn(ExperimentalCoroutinesApi::class)
inline fun runTestViewModelScope(crossinline block: suspend TestScope.() -> Unit) = runReliableTest {
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    Dispatchers.setMain(testDispatcher)

    block()
}


/**
 * The default [runTest] will swallow thrown [Throwable]s that occur within
 * [ViewModel].viewModelScope.launch calls. Thus, you may have [Throwable]s being thrown
 * and and yet passing unit tests. This helper method works around the issue by listening to the
 * Thread's UncaughtExceptionHandler and propagating the exception to fail the unit test.
 *
 * [This has been a known issue for the last 3 years.](https://github.com/Kotlin/kotlinx.coroutines/issues/1205)
 */
fun runReliableTest(block: suspend TestScope.() -> Unit) = runTest {
    // Capture exceptions that may occur in `testBody()` since `runTest` will swallow
    // them if the exception occurs within another scope even though all dispatchers
    // should run without any delays - https://github.com/Kotlin/kotlinx.coroutines/issues/1205
    var error: Throwable? = null
    Thread.setDefaultUncaughtExceptionHandler { _, throwable -> error = throwable }

    // Run our actual test code
    block()

    // Throw exception here, otherwise it will be wrapped and hard to read if thrown from
    // within `Thread.setDefaultUncaughtExceptionHandler`
    error?.let { throw it }
}
