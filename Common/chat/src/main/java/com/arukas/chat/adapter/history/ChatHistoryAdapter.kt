package com.arukas.chat.adapter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.arukas.base.BaseListAdapter
import com.arukas.chat.databinding.ItemRoomBinding
import com.arukas.network.model.Single

class ChatHistoryAdapter(private val myUserId:String): BaseListAdapter<Single, ChatHistoryViewHolder>() {
    override fun getViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryViewHolder {
        val binding=ItemRoomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatHistoryViewHolder(binding,myUserId)
    }
}