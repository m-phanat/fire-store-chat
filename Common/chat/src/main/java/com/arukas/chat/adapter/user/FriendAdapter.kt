package com.arukas.chat.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arukas.chat.adapter.user.view_holder.AddFriendViewHolder
import com.arukas.chat.adapter.user.view_holder.FriendViewHolder
import com.arukas.chat.databinding.ItemAddFriendBinding
import com.arukas.chat.databinding.ItemFriendBinding
import com.arukas.network.model.Person

class FriendAdapter(
    private val onItemClicked: ((user: Person) -> Unit)?
) :
    RecyclerView.Adapter<FriendViewHolder>() {
    private var userList = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding =
            ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserLis(users: List<Person>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun removeItem(user: Person) {
        val index = userList.indexOfFirst { it.objectId == user.objectId }
        if (index != -1) {
            userList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}