package com.arukas.base.common_error

import android.app.Application
import androidx.annotation.StringRes

data class ErrorMessage(var code: Int = ErrorConstant.COMMON_ERROR, var message: String = "") {
    override fun toString(): String {
        return message
    }
}