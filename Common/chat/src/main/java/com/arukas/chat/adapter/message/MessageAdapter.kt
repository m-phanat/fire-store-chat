package com.arukas.chat.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arukas.base.BaseListAdapter
import com.arukas.base.BaseViewHolder
import com.arukas.chat.databinding.ItemMessageLeftBinding
import com.arukas.chat.databinding.ItemMessageRightBinding
import com.arukas.network.model.Message
import com.arukas.network.utils.UserManager

class MessageAdapter : BaseListAdapter<Message, BaseViewHolder<Message, *>>() {
    private val myUser = UserManager.getInstance().getUser()

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int

    ): BaseViewHolder<Message, *> {
        return when (viewType) {
            MessageViewType.VIEW_TYPE_TEXT_LEFT -> {
                LeftTextMessageViewHolder(
                    ItemMessageLeftBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                RightTextMessageViewHolder(
                    ItemMessageRightBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.userId != myUser?.objectId) {
            //left
            return when (item.type) {
                "text" -> {
                    MessageViewType.VIEW_TYPE_TEXT_LEFT
                }

                else -> {
                    -1
                }
            }
        } else {
            //right
            return when (item.type) {
                "text" -> {
                    MessageViewType.VIEW_TYPE_TEXT_RIGHT
                }
                else -> {
                    -1
                }
            }
        }
    }
}

object MessageViewType {
    const val VIEW_TYPE_TEXT_LEFT = 1
    const val VIEW_TYPE_TEXT_RIGHT = 2
}