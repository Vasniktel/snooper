package com.github.vasniktel.snooper.ui.user

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.ViewState

interface UserViewStateCallback {
    fun onUserLoaded(user: User)
    fun onError(message: String, throwable: Throwable?)
}

typealias UserViewState = ViewState<UserViewStateCallback>

data class ErrorState(
    private val message: String,
    private val throwable: Throwable? = null
) : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onError(message, throwable)
    }
}

data class LoadedUser(
    private val user: User
) : UserViewState {
    override fun applyCallback(callback: UserViewStateCallback) {
        callback.onUserLoaded(user)
    }
}
