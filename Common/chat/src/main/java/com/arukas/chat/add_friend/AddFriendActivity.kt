package com.arukas.chat.add_friend

import android.app.Activity
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arukas.base.BaseActivity
import com.arukas.chat.R
import com.arukas.chat.adapter.user.AddFriendAdapter
import com.arukas.chat.databinding.ActivityAddFriendBinding
import com.arukas.network.model.Person


class AddFriendActivity : BaseActivity<ActivityAddFriendBinding, AddFriendActivityViewModel>() {
    override val binding: ActivityAddFriendBinding by lazy {
        ActivityAddFriendBinding.inflate(
            layoutInflater
        )
    }
    override val viewModel: AddFriendActivityViewModel by viewModels()
    private val adapter by lazy { AddFriendAdapter(this::onUserClicked, this::onAddClicked) }

    private fun onUserClicked(user: Person) {
        //
    }

    private fun onAddClicked(user: Person) {
        user.isAdded = true
        adapter.notifyDataSetChanged()
        viewModel.addFriend(user) {
            showAlert("Friend added")
            adapter.removeItem(user)
        }
    }

    override fun initView() {
        observeUsers()
        initToolbar()
        initRefresh()
        initUserList()

        viewModel.loadUser()
    }

    private fun initRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            binding.toolbar.collapseActionView()
            viewModel.setKeyword("")
            viewModel.loadUser()
        }
    }

    private fun initUserList() {
        binding.rcvUser.setHasFixedSize(true)
        binding.rcvUser.layoutManager = LinearLayoutManager(this)
        binding.rcvUser.adapter = adapter
    }

    private fun observeUsers() {
        viewModel.getUsers().observe(this, Observer {
            adapter.setUserLis(it)
        })
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (binding.toolbar.isOverflowMenuShowing) {
            binding.toolbar.hideOverflowMenu()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_friend, menu)

        val menuSearch = menu?.findItem(R.id.menuSearch)
        val searchView = menuSearch?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setKeyword(query.orEmpty())
                viewModel.loadUser()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

}