package com.arukas.chatdemo.view.splash

import android.os.Handler
import androidx.activity.viewModels
import com.arukas.base.BaseActivity
import com.arukas.base.navigator.AppNavigator
import com.arukas.chatdemo.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityViewModel>() {
    override val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: SplashActivityViewModel by viewModels()
    private var handler: Handler? = Handler()
    private val delayTask = Runnable {
        if (viewModel.isLoggedIn()) {
            (application as AppNavigator).onLogin()
        } else {
            (application as AppNavigator).onLogout()
        }
    }

    override fun initView() {
        handler?.postDelayed(delayTask, 2000L)
    }


    override fun destroyView() {
        handler?.removeCallbacks(delayTask)
        handler = null
    }
}