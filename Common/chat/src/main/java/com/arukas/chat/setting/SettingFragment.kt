package com.arukas.chat.setting

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arukas.base.BaseFragment
import com.arukas.base.navigator.AppNavigator
import com.arukas.chat.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding, SettingFragmentViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = SettingFragment()
    }

    override val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: SettingFragmentViewModel by viewModels()

    override fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.imgLogout.setOnClickListener {
            viewModel.logout()
            (requireContext().applicationContext as AppNavigator).onLogout()
        }
    }
}