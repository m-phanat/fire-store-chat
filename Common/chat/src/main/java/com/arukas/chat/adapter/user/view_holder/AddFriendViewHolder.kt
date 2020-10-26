package com.arukas.chat.adapter.user.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.arukas.chat.R
import com.arukas.chat.databinding.ItemAddFriendBinding
import com.arukas.network.glide.GlideApp
import com.arukas.network.model.Person
import com.arukas.network.utils.FireStorageManager

class AddFriendViewHolder(
    private val binding: ItemAddFriendBinding,
    private var onItemClicked: ((user: Person) -> Unit)?,
    private var onAddClicked: ((user: Person) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: Person) {
        val imageRef =
            FireStorageManager.getInstance().getUserImageReference(user.objectId.orEmpty())
        val displayName = user.fullName
        val userName = user.email

        GlideApp.with(binding.imgProfile)
            .load(imageRef)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(binding.imgProfile)

        binding.tvDisplayName.text = displayName
        binding.tbxUsername.text = userName

        binding.root.setOnClickListener {
            onItemClicked?.invoke(user)
        }

        if(user.isAdded){
            binding.imgAdd.alpha=0.4f
            binding.imgAdd.setOnClickListener(null)
        } else {
            binding.imgAdd.alpha=1f
            binding.imgAdd.setOnClickListener {
                onAddClicked?.invoke(user)
            }
        }
    }
}