package com.arukas.chat.adapter.message

import android.text.format.DateUtils
import com.arukas.base.BaseViewHolder
import com.arukas.chat.R
import com.arukas.chat.databinding.ItemMessageLeftBinding
import com.arukas.network.glide.GlideApp
import com.arukas.network.model.Message
import com.arukas.network.utils.FireStorageManager

class LeftTextMessageViewHolder(binding: ItemMessageLeftBinding) :
    BaseViewHolder<Message, ItemMessageLeftBinding>(binding) {
    override fun bind(item: Message) {
        val profileImageRef =
            FireStorageManager.getInstance().getUserImageReference(item.userId.orEmpty())
        GlideApp.with(binding.imgProfile)
            .load(profileImageRef)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(binding.imgProfile)

        binding.tvDateTime.text =
            DateUtils.getRelativeTimeSpanString(binding.root.context, item.updatedAt ?: 0L)
        binding.tvMessage.text = item.text
    }
}