package com.github.vasniktel.snooper.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import kotlinx.android.synthetic.main.user_list_item.view.*

private object UserListDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

typealias UserListItemClickListener = (User, Int) -> Unit

class UserListAdapter(
    private val onItemClick: UserListItemClickListener
) : ListAdapter<User, UserViewHolder>(UserListDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, position)
    }
}

class UserViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {
    fun bind(user: User, onItemClick: UserListItemClickListener, position: Int) {
        view.userName.text = user.name
        view.userListItem.setOnClickListener { onItemClick(user, position) }
    }
}
