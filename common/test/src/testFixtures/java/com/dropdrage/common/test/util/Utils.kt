package com.dropdrage.common.test.util

import sun.misc.Unsafe
import java.lang.reflect.Field

inline fun <T> setStaticFields(vararg fields: Triple<Field, T, T>, block: () -> Unit) {
    fields.forEach { setFinalStatic(it.first, it.third) }
    block()
    fields.forEach { setFinalStatic(it.first, it.second) }
}


fun setFinalStatic(field: Field, newValue: Any?) {
    try {
        val unsafeField: Field = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        setFinalStatic1(unsafeField[null] as Unsafe, field, newValue)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun setFinalStatic1(unsafe: Unsafe, field: Field, value: Any?) {
    val fieldBase: Any = unsafe.staticFieldBase(field)
    val fieldOffset: Long = unsafe.staticFieldOffset(field)
    unsafe.putObject(fieldBase, fieldOffset, value)
}
