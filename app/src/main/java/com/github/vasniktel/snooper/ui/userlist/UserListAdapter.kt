package com.github.vasniktel.snooper.ui.userlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.User
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.message_list_item.view.*
import kotlinx.android.synthetic.main.user_list_item.view.*
import kotlinx.android.synthetic.main.user_list_item.view.userName
import kotlinx.android.synthetic.main.user_list_item.view.userPhoto
import java.lang.Exception

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
    fun bind(
        user: User,
        onItemClick: UserListItemClickListener,
        position: Int
    ) = with(view) {
        userName.text = user.name
        userListItem.setOnClickListener { onItemClick(user, position) }

        Picasso.get()
            // Picasso doesn't load vector drawables if url is null
            .load(user.photoUrl ?: "load error drawable")
            .error(R.drawable.ic_baseline_person_24)
            .resizeDimen(R.dimen.user_list_item_photo, R.dimen.user_list_item_photo)
            .transform(CropCircleTransformation())
            .into(userPhoto)
    }
}
