package com.soussidev.kotlin.rxprogress.util

import android.content.Context
import android.util.TypedValue

/**
 * Created by Soussi on 12/10/2017.
 */
object DensityConverter {

    /**
     * Convert a DP value back to pixels.

     * @param context the application context.
     * *
     * @param dp the density pixels to convert.
     * *
     * @return the associated pixel value.
     */
    fun toPx(context: Context, dp: Int): Int {
        return convert(context, dp, TypedValue.COMPLEX_UNIT_PX)
    }

    /**
     * Convert a PX value to density pixels.

     * @param context the application context.
     * *
     * @param px the pixels to convert.
     * *
     * @return the associated density pixel value.
     */
    fun toDp(context: Context, px: Int): Int {
        return convert(context, px, TypedValue.COMPLEX_UNIT_DIP)
    }

    private fun convert(context: Context, amount: Int, conversionUnit: Int): Int {
        if (amount < 0) {
            throw IllegalArgumentException("px should not be less than zero")
        }

        val r = context.resources
        return TypedValue.applyDimension(conversionUnit, amount.toFloat(), r.displayMetrics).toInt()
    }
}