package com.dropdrage.simpleweather.common.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import com.dropdrage.simpleweather.common.presentation.R

sealed class TextMessage {

    abstract fun getMessage(context: Context): String


    object EmptyMessage : TextMessage() {
        override fun getMessage(context: Context): String = ""
    }

    object UnknownErrorMessage : TextMessage() {
        override fun getMessage(context: Context): String = context.getString(R.string.error_unexpected)
    }

    object NoDataAvailableErrorMessage : TextMessage() {
        override fun getMessage(context: Context): String = context.getString(R.string.error_no_data_available)
    }
}

class StringMessage(private val message: String) : TextMessage() {
    override fun getMessage(context: Context): String = message
}

class ResourceMessage(@StringRes private val messageId: Int) : TextMessage() {
    override fun getMessage(context: Context): String = context.getString(messageId)
}


fun String.toTextMessage(): TextMessage = StringMessage(this)

fun String?.toTextMessageOrUnknownErrorMessage(): TextMessage =
    if (this != null) StringMessage(this)
    else TextMessage.UnknownErrorMessage

fun Int.toTextMessage(): TextMessage = ResourceMessage(this)
