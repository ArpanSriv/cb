package com.arpan.collegebroker

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

fun switchImageToggleState(view: ImageView, outState: Boolean) {
    if (!outState) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        view.colorFilter = filter
    } else {
        view.clearColorFilter()
    }
}