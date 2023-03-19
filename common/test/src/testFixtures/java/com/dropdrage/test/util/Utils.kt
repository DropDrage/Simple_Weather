package com.dropdrage.test.util

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Modifier

inline fun setSdk(sdk: Int, block: () -> Unit) {
    setStaticField(Build.VERSION::class.java.getField("SDK_INT"), Build.VERSION.SDK_INT, sdk, block)
}

suspend inline fun setSdkSuspend(sdk: Int, crossinline block: suspend () -> Unit) {
    val realSdk = Build.VERSION.SDK_INT
    setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), sdk)
    block()
    resetFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), realSdk)
}

inline fun <T> setStaticField(field: Field, realValue: T, newValue: T, block: () -> Unit) {
    setFinalStatic(field, newValue)
    block()
    resetFinalStatic(field, realValue)
}

inline fun <T> setStaticFields(fields: Array<Triple<Field, T, T>>, block: () -> Unit) {
    fields.forEach { setFinalStatic(it.first, it.third) }
    block()
    fields.forEach { setFinalStatic(it.first, it.second) }
}


fun setFinalStatic(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())
    field.set(null, newValue)
}

fun resetFinalStatic(field: Field, oldValue: Any?) {
    field.isAccessible = true
    val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
    modifiersField.setAccessible(true)
    modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL.inv())
    field.set(null, oldValue)
    modifiersField.setInt(field, field.getModifiers() and Modifier.FINAL)
    modifiersField.isAccessible = false
    field.isAccessible = false
}
