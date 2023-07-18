package com.example.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.core.remote.response.UserResponseItem
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.utils.loadImage

class UserAdapter(var onCLick: ((UserResponseItem) -> Unit)? = null) :
    ListAdapter<UserResponseItem, UserAdapter.UserViewHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponseItem?) {
            binding.tvUsername.text = user?.login
            binding.shapeableImageView.loadImage(user?.avatarUrl)
        }

        init {
            binding.root.setOnClickListener {
                onCLick?.invoke(getItem(adapterPosition))
            }
        }
    }

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<UserResponseItem> =
            object : DiffUtil.ItemCallback<UserResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: UserResponseItem,
                    newItem: UserResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: UserResponseItem,
                    newItem: UserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}