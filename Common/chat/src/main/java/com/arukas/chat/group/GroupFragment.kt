package com.arukas.chat.group

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arukas.base.BaseFragment
import com.arukas.chat.databinding.FragmentGroupBinding

class GroupFragment: BaseFragment<FragmentGroupBinding, GroupFragmentViewModel>() {
    companion object{
        @JvmStatic
        fun newInstance():Fragment=GroupFragment()
    }

    override val binding: FragmentGroupBinding by lazy { FragmentGroupBinding.inflate(layoutInflater) }
    override val viewModel: GroupFragmentViewModel by viewModels()

    override fun initView() {
        //
    }
}