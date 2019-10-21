package com.plusmobileapps.location

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

fun Int.dpToInt(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)
        .roundToInt()
}
