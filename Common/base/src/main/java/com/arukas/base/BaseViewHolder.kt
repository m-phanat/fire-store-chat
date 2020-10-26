package com.arukas.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T,BD:ViewBinding>(protected var binding:BD):RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item:T)
}