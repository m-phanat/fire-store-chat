package com.arukas.chat.adapter.message

import android.text.format.DateUtils
import com.arukas.base.BaseViewHolder
import com.arukas.chat.databinding.ItemMessageRightBinding
import com.arukas.network.model.Message

class RightTextMessageViewHolder(binding: ItemMessageRightBinding) :
    BaseViewHolder<Message, ItemMessageRightBinding>(binding) {
    override fun bind(item: Message) {
        binding.tvDateTime.text =
            DateUtils.getRelativeTimeSpanString(binding.root.context, item.createdAt ?: 0L)
        binding.tvMessage.text = item.text
    }
}