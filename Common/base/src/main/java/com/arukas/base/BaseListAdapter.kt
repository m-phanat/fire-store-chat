package com.arukas.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T,VH: BaseViewHolder<T, *>>:RecyclerView.Adapter<VH>() {
    private var mData= mutableListOf<T>()

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return getViewHolder(parent,viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(mData[position])
    }

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int):VH

    fun addData(data:List<T>){
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setData(data: List<T>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        mData.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int):T{
        return mData[position]
    }
}