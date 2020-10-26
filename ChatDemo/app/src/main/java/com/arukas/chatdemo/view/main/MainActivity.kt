package com.arukas.chatdemo.view.main

import android.content.Intent
import androidx.activity.viewModels
import com.arukas.base.BaseActivity
import com.arukas.chat.main.MainChatActivity
import com.arukas.chatdemo.R
import com.arukas.chatdemo.databinding.ActivityMainBinding
import com.arukas.authen.login.LoginActivity
import com.arukas.network.glide.GlideApp
import com.arukas.network.utils.FireStorageManager

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override val viewModel: MainActivityViewModel by viewModels()

    override fun initView() {
        observeUserProfile()
        initToolbar()
        initMenu()

        viewModel.loadUser()
    }

    private fun observeUserProfile() {
        viewModel.getUser().observe(this, {
            val imageRef = FireStorageManager.getInstance().getUserImageReference(it.objectId.orEmpty())
            GlideApp.with(this@MainActivity)
                .load(imageRef)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(binding.imgProfile)

            binding.tvDisplayName.text = "${it.fullName}"
        })
    }

    private fun initMenu() {
        binding.cardChat.setOnClickListener {
            val chatIntent = Intent(this@MainActivity, MainChatActivity::class.java)
            startActivity(chatIntent)
        }
    }

    private fun initToolbar() {
        binding.imgLogout.setOnClickListener {
            viewModel.logout()
            val loginIntent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            startActivity(loginIntent)
        }
    }

}