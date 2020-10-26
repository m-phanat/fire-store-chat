package com.arukas.chat.create_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.arukas.base.BaseActivity
import com.arukas.chat.R
import com.arukas.chat.databinding.ActivityCreateRoomBinding

class CreateRoomActivity : BaseActivity<ActivityCreateRoomBinding,CreateRoomActivityViewModel>(){
    override val binding: ActivityCreateRoomBinding by lazy {
        ActivityCreateRoomBinding.inflate(layoutInflater)
    }
    override val viewModel: CreateRoomActivityViewModel by viewModels()

    override fun initView() {
        //
    }

}