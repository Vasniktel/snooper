package com.github.vasniktel.snooper.ui.user

import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.mvi.ViewEvent

interface UserViewEventCallback {
    fun onUpdateSubscriptionEvent(user: User)
    fun onChangeSubscriptionEvent(user: User, isFollowee: Boolean)
    fun onPostMessageEvent()
    fun onLogOutEvent()
}

typealias UserViewEvent = ViewEvent<UserViewEventCallback>

data class UpdateSubscriptionEvent(
    private val user: User
) : UserViewEvent {
    override fun applyCallback(callback: UserViewEventCallback) {
        callback.onUpdateSubscriptionEvent(user)
    }
}

data class ChangeSubscriptionsEvent(
    private val user: User,
    private val isFollowee: Boolean
) : UserViewEvent {
    override fun applyCallback(callback: UserViewEventCallback) {
        callback.onChangeSubscriptionEvent(user, isFollowee)
    }
}

object PostMessageEvent : UserViewEvent {
    override fun applyCallback(callback: UserViewEventCallback) {
        callback.onPostMessageEvent()
    }
}

object LogOutEvent : UserViewEvent {
    override fun applyCallback(callback: UserViewEventCallback) {
        callback.onLogOutEvent()
    }
}
