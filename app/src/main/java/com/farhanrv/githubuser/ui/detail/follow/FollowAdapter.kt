package com.farhanrv.githubuser.ui.detail.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhanrv.githubuser.data.network.response.FollowResponse
import com.farhanrv.githubuser.databinding.ItemFollowListBinding

class FollowAdapter : ListAdapter<FollowResponse, FollowAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder =
        ItemViewholder(
            ItemFollowListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(private val binding: ItemFollowListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FollowResponse) = with(binding) {
            tvGithubUsername.text = item.login
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .circleCrop()
                .into(imgAvatar)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<FollowResponse>() {
    override fun areItemsTheSame(oldItem: FollowResponse, newItem: FollowResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FollowResponse, newItem: FollowResponse): Boolean {
        return oldItem == newItem
    }
}