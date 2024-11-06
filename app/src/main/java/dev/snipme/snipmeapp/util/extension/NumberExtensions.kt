package dev.snipme.snipmeapp.util.extension

import android.graphics.Color

val Int.maxAlpha get() = 255

val Int.red get() = Color.red(this)

val Int.green get() = Color.green(this)

val Int.blue get() = Color.blue(this)

val Int.rgb get() = Color.rgb(red, green, blue)

val Int.opaque: Int
    get() = Color.argb(maxAlpha, red, green, blue)

fun Int.transparent(alpha: Float) = Color.argb((maxAlpha * alpha).toInt(), red, green, blue)

