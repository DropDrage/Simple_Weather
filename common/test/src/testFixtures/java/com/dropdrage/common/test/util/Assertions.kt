package com.dropdrage.common.test.util

import org.junit.jupiter.api.Assertions.assertInstanceOf
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <reified T> assertInstanceOf(value: Any) {
    contract {
        returns() implies (value is T)
    }

    assertInstanceOf(T::class.java, value)
}
