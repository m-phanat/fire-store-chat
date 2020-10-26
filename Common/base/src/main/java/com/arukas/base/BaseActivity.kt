package com.arukas.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.arukas.base.utils.AlertUtils

abstract class BaseActivity<BD : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    protected abstract val binding: BD
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeError()
        initView()
    }

    abstract fun initView()

    open fun observeError() {
        viewModel.getError().observe(this, Observer {
            showAlert(it.toString())
        })
    }

    protected fun showAlert(message:String,onConfirm:(()->Unit)?=null){
        AlertUtils.showAlert(this@BaseActivity, getString(R.string.app_name), message,onConfirm)
    }

    override fun onDestroy() {
        destroyView()
        super.onDestroy()
    }

    open fun destroyView() {}
}