package com.development.githubuser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.development.githubuser.databinding.ItemUserBinding
import com.development.githubuser.data.remote.response.ItemsItem

class MainAdapter(
    private val data: MutableList<ItemsItem> = mutableListOf(),
    private val listener: (ItemsItem) -> Unit
) : RecyclerView.Adapter<MainAdapter.UserViewHolder>() {

    class UserViewHolder(
        private val v: ItemUserBinding
    ) : RecyclerView.ViewHolder(v.root) {

        fun bind(item: ItemsItem) {
            v.image.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }
            v.username.text = item.login
        }
    }

    fun setData(data: MutableList<ItemsItem>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

}