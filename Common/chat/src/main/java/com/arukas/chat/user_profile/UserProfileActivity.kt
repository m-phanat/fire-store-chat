package com.arukas.chat.user_profile

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.arukas.base.BaseActivity
import com.arukas.chat.R
import com.arukas.chat.databinding.ActivityUserProfileBinding
import com.arukas.chat.room.ChatRoomActivity
import com.arukas.common.ChatConstant
import com.arukas.network.glide.GlideApp
import com.arukas.network.model.Person

class UserProfileActivity :
    BaseActivity<ActivityUserProfileBinding, UserProfileActivityViewModel>() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, user: Person): Intent {
            return Intent(context, UserProfileActivity::class.java).apply {
                putExtra(ChatConstant.EXTRA_USER, user)
            }
        }
    }

    override val binding: ActivityUserProfileBinding by lazy {
        ActivityUserProfileBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: UserProfileActivityViewModel by viewModels()

    override fun initView() {
        initToolbar()
        initData()
        initUserProfile()
        initMenu()
    }

    private fun initMenu() {
        binding.cardChat.setOnClickListener {
            viewModel.createChatRoom { room ->
                startActivity(ChatRoomActivity.createIntent(this@UserProfileActivity, room))
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun initToolbar() {
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initUserProfile() {
        GlideApp.with(this)
            .load(viewModel.getUserProfileImage())
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(binding.imgProfile)

        binding.tvDisplayName.text = viewModel.getDisplayName()
    }

    private fun initData() {
        intent.getParcelableExtra<Person>(ChatConstant.EXTRA_USER)?.let {
            viewModel.setUser(it)
        }
    }
}