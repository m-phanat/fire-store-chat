package com.arukas.chat.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arukas.base.BaseFragment
import com.arukas.chat.adapter.history.ChatHistoryAdapter
import com.arukas.chat.databinding.FragmentChatHistoryBinding

class ChatHistoryFragment :
    BaseFragment<FragmentChatHistoryBinding, ChatHistoryFragmentViewModel>() {
    companion object {
        @JvmStatic
        fun newInstance(): Fragment = ChatHistoryFragment()
    }

    override val binding: FragmentChatHistoryBinding by lazy {
        FragmentChatHistoryBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: ChatHistoryFragmentViewModel by viewModels()
    private val adapter by lazy { ChatHistoryAdapter(viewModel.getMyUserId()) }

    override fun initView() {
        observeChatHistory()
        observeHistoryUpdate()
        initRefresh()
        initChatHistoryList()

        viewModel.loadChatHistory()
    }

    private fun observeHistoryUpdate() {
        viewModel.getHistoryUpdate().observe(viewLifecycleOwner, {
            adapter.updateData(it)
        })
    }

    private fun observeChatHistory() {
        viewModel.getChatHistory().observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    private fun initRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing=false
            viewModel.loadChatHistory()
        }
    }

    private fun initChatHistoryList() {
        binding.rcvChatHistory.setHasFixedSize(true)
        binding.rcvChatHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvChatHistory.adapter = adapter
    }
}