package com.arukas.base.utils

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import com.arukas.base.R

object AlertUtils {
    @JvmStatic
    fun showAlert(context: Context, title: String, message: String, onOk: (() -> Unit)? = null) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ ->
                onOk?.invoke()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    @JvmStatic
    fun showAlert(
        context: Context,
        @StringRes titleRes: Int,
        @StringRes msgRes: Int,
        onOk: (() -> Unit)? = null
    ) {
        showAlert(context, context.getString(titleRes), context.getString(msgRes), onOk)
    }
}