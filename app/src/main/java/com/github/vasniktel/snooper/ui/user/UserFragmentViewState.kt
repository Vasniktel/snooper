package com.github.vasniktel.snooper.ui.user

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.ViewState

interface UserViewStateCallback {
    fun onSubscriptionUpdate(isFollowee: Boolean)
    fun onPopulateState()
}

typealias UserViewState = ViewState<UserViewStateCallback>

object PopulateState : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onPopulateState()
    }
}

data class SubscriptionUpdate(
    val isFollowee: Boolean
) : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onSubscriptionUpdate(isFollowee)
    }
}
