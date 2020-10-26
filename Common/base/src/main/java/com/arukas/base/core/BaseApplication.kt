package com.arukas.base.core

import android.app.Application
import com.arukas.base.navigator.AppNavigator

abstract class BaseApplication : Application(), AppNavigator {

    override fun onCreate() {
        super.onCreate()

        createApp()
    }

    abstract fun createApp()
}