package com.github.vasniktel.snooper.ui.messagelist

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.vasniktel.snooper.R
import com.github.vasniktel.snooper.logic.model.Message
import kotlinx.android.synthetic.main.message_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

private object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Message, newItem: Message) =
        oldItem == newItem
}

interface ListItemCallback {
    fun onUserClicked(position: Int, message: Message)
    fun onMessageMenuButtonClicked(position: Int, message: Message)
    fun onMessageLikeButtonClicked(position: Int, message: Message)
    fun onMessageShareButtonClicked(position: Int, message: Message)
    fun onMapClicked(position: Int, message: Message)
}

class MessageListAdapter(
    private val callback: ListItemCallback
    //private val currentUserProvider: () -> User
) : ListAdapter<Message, MessageFeedViewHolder>(
    MessageDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageFeedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_list_item, parent, false)
        return MessageFeedViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: MessageFeedViewHolder, position: Int) {
        holder.bind(getItem(position)!!, callback, position)
    }
}

private val DATE_FORMATTER = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.US)

class MessageFeedViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {
    fun bind(
        data: Message,
        callback: ListItemCallback,
        position: Int
    ): Unit = with(view) {
        userName.text = data.ownerName
        messageDate.text = DATE_FORMATTER.format(data.date)

        likeCount.text = data.likes.toString()
        if (data.likes > 0) {
            val color = ContextCompat.getColor(context, R.color.colorAccent)
            likeCount.setTextColor(color)
            messageLikeButton.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        } else {
            likeCount.setTextColor(likeCount.textColors.defaultColor)
            messageLikeButton.clearColorFilter()
        }

        /*if (currentUser is User.LoggedIn && currentUser.id == data.ownerId) {
            messageMenuButton!!.visibility = View.VISIBLE
        } else {
            messageMenuButton!!.visibility = View.GONE
        }*/

        userPhoto.setOnClickListener { callback.onUserClicked(position, data) }
        userName.setOnClickListener { callback.onUserClicked(position, data) }
        messageMenuButton.setOnClickListener { callback.onMessageMenuButtonClicked(position, data) }
        messageLikeButton.setOnClickListener { callback.onMessageLikeButtonClicked(position, data) }
        messageShareButton.setOnClickListener { callback.onMessageShareButtonClicked(position, data) }
    }
}
