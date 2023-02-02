package com.dropdrage.common.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

fun Context.getBitmapFromDrawable(@DrawableRes drawable: Int): Bitmap {
    val db = getDrawable(drawable)
    val bit = Bitmap.createBitmap(db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bit)

    db.setBounds(0, 0, canvas.width, canvas.height)
    db.draw(canvas)

    return bit
}


fun Context.showToast(@StringRes textRes: Int) {
    Toast.makeText(this, textRes, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
