package com.arukas.chat.main

import android.util.SparseArray
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.arukas.base.BaseActivity
import com.arukas.chat.R
import com.arukas.chat.adapter.main.MainChatAdapter
import com.arukas.chat.databinding.ActivityMainChatBinding
import com.arukas.chat.group.GroupFragment
import com.arukas.chat.history.ChatHistoryFragment
import com.arukas.chat.people.PeopleFragment
import com.arukas.chat.setting.SettingFragment

class MainChatActivity : BaseActivity<ActivityMainChatBinding, MainChatActivityViewModel>() {
    override val binding: ActivityMainChatBinding by lazy {
        ActivityMainChatBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: MainChatActivityViewModel by viewModels()

    private val adapter by lazy {
        val fragments = SparseArray<Fragment>()
        fragments.put(0, ChatHistoryFragment.newInstance())
        fragments.put(1, PeopleFragment.newInstance())
        fragments.put(2, GroupFragment.newInstance())
        fragments.put(3, SettingFragment.newInstance())
        MainChatAdapter(supportFragmentManager, lifecycle, fragments)
    }

    private val onPageChange = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.navChat.menu.getItem(position).isChecked = true
        }
    }

    override fun initView() {
        initTab()
    }

    private fun initTab() {
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(onPageChange)

        binding.navChat.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuChat -> {
                    binding.viewPager.currentItem = 0
                }

                R.id.menuPeople -> {
                    binding.viewPager.currentItem = 1
                }

                R.id.menuGroup -> {
                    binding.viewPager.currentItem = 2
                }

                else -> {
                    binding.viewPager.currentItem = 3
                }
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun destroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(onPageChange)
    }

    fun setActive(position: Int) {
        binding.viewPager.setCurrentItem(position,false)
    }
}