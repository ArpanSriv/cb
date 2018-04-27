package com.arpan.collegebroker

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import java.io.CharArrayReader

class Prefs(context: Context) {

    val PREFS_FILENAME = "com.arpan.collegebroker.shared"
    val CATEGORY = "CATEGORY"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var category: Int
        get() = prefs.getInt(CATEGORY, 1)
        set(value) = prefs.edit().putInt(CATEGORY, value).apply()
}
