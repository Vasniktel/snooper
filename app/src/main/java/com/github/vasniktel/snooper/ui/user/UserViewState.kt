package com.github.vasniktel.snooper.ui.user

import com.github.vasniktel.snooper.util.mvi.ViewState

interface UserViewStateCallback {
    fun onSubscriptionUpdateState(isFollowee: Boolean)
    fun onPopulateState()
    fun onError(message: String, throwable: Throwable?)
}

typealias UserViewState = ViewState<UserViewStateCallback>

object PopulateState : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onPopulateState()
    }
}

data class SubscriptionUpdateState(
    val isFollowee: Boolean
) : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onSubscriptionUpdateState(isFollowee)
    }
}

data class ErrorState(
    private val message: String,
    private val throwable: Throwable? = null
) : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onError(message, throwable)
    }
}
