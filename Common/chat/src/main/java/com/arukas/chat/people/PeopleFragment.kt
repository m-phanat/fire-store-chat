package com.arukas.chat.people

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arukas.base.BaseFragment
import com.arukas.chat.adapter.user.FriendAdapter
import com.arukas.chat.add_friend.AddFriendActivity
import com.arukas.chat.databinding.FragmentPeopleBinding
import com.arukas.chat.main.MainChatActivity
import com.arukas.chat.user_profile.UserProfileActivity
import com.arukas.common.ChatConstant
import com.arukas.network.model.Person

class PeopleFragment : BaseFragment<FragmentPeopleBinding, PeopleFragmentViewModel>() {
    companion object {
        @JvmStatic
        fun newInstance(): Fragment = PeopleFragment()
    }

    override val binding: FragmentPeopleBinding by lazy {
        FragmentPeopleBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: PeopleFragmentViewModel by viewModels()
    private val adapter by lazy { FriendAdapter(this::onFriendClicked) }

    private fun onFriendClicked(user: Person) {
        startActivityForResult(UserProfileActivity.createIntent(requireContext(), user),ChatConstant.REQUEST_CHAT)
    }

    override fun initView() {
        observeFriends()
        initToolbar()
        initRefresh()
        initFriendList()

        viewModel.loadFriend()
    }

    private fun observeFriends() {
        viewModel.getFriends().observe(viewLifecycleOwner, Observer {
            adapter.setUserLis(it)
        })
    }

    private fun initRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            viewModel.loadFriend()
        }
    }

    private fun initFriendList() {
        binding.rcvFriend.setHasFixedSize(true)
        binding.rcvFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvFriend.adapter = adapter
    }

    private fun initToolbar() {
        binding.imgAdd.setOnClickListener {
            val addFriendIntent = Intent(requireContext(), AddFriendActivity::class.java)
            startActivity(addFriendIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                ChatConstant.REQUEST_CHAT->{
                    val mainActivity=activity as? MainChatActivity
                    mainActivity?.setActive(0)
                }

                else ->{}
            }
        }
    }
}