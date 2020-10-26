package com.arukas.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.common_error.ErrorConstant
import com.arukas.base.common_error.ErrorMessage

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val error = MutableLiveData<ErrorMessage>()

    fun getError(): LiveData<ErrorMessage> {
        return error
    }

    fun postError(msg: String) {
        postErrorWithCode(ErrorConstant.COMMON_ERROR, msg)
    }

    fun postErrorWithCode(code: Int, msg: String) {
        error.value = ErrorMessage(code, msg)
    }

    fun postError(@StringRes msgRes: Int) {
        postErrorWithCode(
            ErrorConstant.COMMON_ERROR,
            getApplication<Application>().getString(msgRes)
        )
    }

    fun postErrorWithCode(code: Int, @StringRes msgRes: Int) {
        postErrorWithCode(code, getApplication<Application>().getString(msgRes))
    }
}