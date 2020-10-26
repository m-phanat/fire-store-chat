package com.arukas.chatdemo.view.splash

import android.app.Application
import com.arukas.base.BaseViewModel
import com.arukas.network.utils.UserManager

class SplashActivityViewModel(application: Application) : BaseViewModel(application) {
    fun isLoggedIn(): Boolean {
        return UserManager.getInstance().isLoggedIn()
    }
}