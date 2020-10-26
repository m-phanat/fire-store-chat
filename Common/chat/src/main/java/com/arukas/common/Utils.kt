package com.arukas.common

import android.text.format.DateUtils

object Utils {

    @JvmStatic
    fun getDisplayDate(timestamp: Long): String {
        return DateUtils.getRelativeTimeSpanString(timestamp).toString()
    }
}