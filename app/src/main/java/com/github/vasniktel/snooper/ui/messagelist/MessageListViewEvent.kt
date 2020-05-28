package com.github.vasniktel.snooper.ui.messagelist

import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.mvi.ViewEvent

interface MessageListViewEventCallback {
    fun onRefreshEvent(user: User?, fetch: Boolean)
    fun onLikeClickedEvent(message: Message)
}

typealias MessageListViewEvent = ViewEvent<MessageListViewEventCallback>

data class RefreshEvent(
    private val user: User?,
    private val fetch: Boolean
) : MessageListViewEvent {
    override fun applyCallback(callback: MessageListViewEventCallback) {
        callback.onRefreshEvent(user, fetch)
    }
}

data class LikeClickedEvent(
    private val message: Message
) : MessageListViewEvent {
    override fun applyCallback(callback: MessageListViewEventCallback) {
        callback.onLikeClickedEvent(message)
    }
}
