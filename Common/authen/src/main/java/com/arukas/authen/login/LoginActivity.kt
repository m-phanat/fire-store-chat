package com.arukas.authen.login

import android.content.Intent
import androidx.activity.viewModels
import com.arukas.authen.databinding.ActivityLoginBinding
import com.arukas.authen.register.RegisterActivity
import com.arukas.base.BaseActivity
import com.arukas.base.navigator.AppNavigator
import com.arukas.network.service.FireStoreObserverService
import com.arukas.network.utils.UserManager

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>() {
    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override val viewModel: LoginActivityViewModel by viewModels()

    override fun initView() {
        if (UserManager.getInstance().isLoggedIn()) {
            openMainPage()
        } else {
            initObserver()
            initWidget()
        }
    }

    private fun initObserver() {
        viewModel.setOnLoginSuccessListener {
            openMainPage()
        }
    }

    private fun openMainPage() {
        (application as AppNavigator).onLogin()
    }

    private fun initWidget() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.tbxUsername.text.toString().orEmpty(),
                binding.tbxPassword.text.toString().orEmpty()
            )
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}