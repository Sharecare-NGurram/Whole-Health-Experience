package com.carelon.whe.widget.rectangular_progress

import android.content.Context
import android.util.TypedValue

object CalculationUtil {
    @JvmStatic
	fun convertDpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        ).toInt()
    }
}