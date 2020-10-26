package com.arukas.chat.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arukas.chat.adapter.user.view_holder.AddFriendViewHolder
import com.arukas.chat.databinding.ItemAddFriendBinding
import com.arukas.network.model.Person

class AddFriendAdapter(
    private val onItemClicked: ((user: Person) -> Unit)?,
    private val onAddClicked: ((user: Person) -> Unit)?
) :
    RecyclerView.Adapter<AddFriendViewHolder>() {
    private var userList = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendViewHolder {
        val binding =
            ItemAddFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddFriendViewHolder(binding, onItemClicked, onAddClicked)
    }

    override fun onBindViewHolder(holder: AddFriendViewHolder, position: Int) {
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