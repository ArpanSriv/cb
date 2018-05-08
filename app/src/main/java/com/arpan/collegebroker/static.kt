package com.arpan.collegebroker

import android.content.Context
import android.util.DisplayMetrics
import com.google.android.gms.common.util.NumberUtils.isNumeric


val FurnishingItems: HashMap<String, Int> = hashMapOf(
        "Sofa" to R.drawable.sofa,
        "TV" to R.drawable.tv,
        "Fridge" to R.drawable.fridge,
        "Washing Machine" to R.drawable.wm,
        "Table" to R.drawable.table,
        "Chair" to R.drawable.chair,
        "Single Bed" to R.drawable.singe_bed,
        "Double Bed" to R.drawable.double_bed
)

fun priceFormatter(inputPrice: String): String {
    try {
        if (!isNumeric(inputPrice)) {
            return inputPrice
        }
        // to check if the number is a decimal number
        var newPrice = ""
        var afterDecimal = ""
        if (inputPrice.indexOf('.') != -1) {
            newPrice = inputPrice.substring(0, inputPrice.lastIndexOf('.'))
            afterDecimal = inputPrice.substring(inputPrice.lastIndexOf('.'))
        } else {
            newPrice = inputPrice
        }
        val length = newPrice.length
        if (length < 4) {
            return inputPrice
        }
        // to check whether the length of the number is even or odd
        var isEven = false
        if (length % 2 == 0) {
            isEven = true
        }
        // to calculate the comma index
        val ch = CharArray(length + (length / 2 - 1))
        if (isEven) {
            var i = 0
            var j = 0
            while (i < length) {
                ch[j++] = inputPrice[i]
                if (i % 2 == 0 && i < length - 3) {
                    ch[j++] = ','
                }
                i++
            }
        } else {
            var i = 0
            var j = 0
            while (i < length) {
                ch[j++] = inputPrice[i]
                if (i % 2 == 1 && i < length - 3) {
                    ch[j++] = ','
                }
                i++
            }
        }
        // conditional return based on decimal number check
        return if (afterDecimal.length > 0) String(ch) + afterDecimal else String(ch)

    } catch (ex: NumberFormatException) {
        ex.printStackTrace()
        return inputPrice
    } catch (e: Exception) {
        e.printStackTrace()
        return inputPrice
    }

}


fun dpToPx(context: Context, dp: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}
