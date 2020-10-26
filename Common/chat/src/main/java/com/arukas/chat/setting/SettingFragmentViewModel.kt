package com.arukas.chat.setting

import android.app.Application
import com.arukas.base.BaseViewModel
import com.arukas.network.utils.UserManager

class SettingFragmentViewModel(application: Application) : BaseViewModel(application) {
    fun logout() {
        UserManager.getInstance().logout()
    }
}