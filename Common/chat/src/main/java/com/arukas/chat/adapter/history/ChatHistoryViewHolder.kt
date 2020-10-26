package com.arukas.chat.adapter.history

import android.util.Log
import android.view.View
import com.arukas.base.BaseViewHolder
import com.arukas.chat.R
import com.arukas.chat.databinding.ItemRoomBinding
import com.arukas.chat.room.ChatRoomActivity
import com.arukas.common.Utils
import com.arukas.network.glide.GlideApp
import com.arukas.network.model.Single
import com.arukas.network.utils.FireStorageManager

class ChatHistoryViewHolder(binding: ItemRoomBinding, private val myUserId: String) :
    BaseViewHolder<Single, ItemRoomBinding>(binding) {

    override fun bind(item: Single) {
        val userId = if (item.userId1 == myUserId) item.userId2 else item.userId1
        val displayName = if (item.userId1 == myUserId) item.fullName2 else item.fullName1
        val profileRef = FireStorageManager.getInstance().getUserImageReference(userId.orEmpty())
        val timestamp = item.updatedAt ?: 0L

        GlideApp.with(binding.imgUser)
            .load(profileRef)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(binding.imgUser)

        binding.tvRoomName.text = displayName
        binding.tvDate.text = Utils.getDisplayDate(timestamp)

        item.lastMessage?.let {
            binding.tvLastMessage.visibility = View.VISIBLE
            binding.tvLastMessage.text=it.text.orEmpty()
        } ?: kotlin.run {
            binding.tvLastMessage.visibility = View.GONE
        }

        binding.tvBadge.visibility = View.GONE

        binding.root.setOnClickListener {
            val context = binding.root.context

            context.startActivity(ChatRoomActivity.createIntent(context, item))
        }
    }
}