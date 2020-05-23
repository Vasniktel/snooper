package com.github.vasniktel.snooper.ui.userlist

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.ui.messagelist.MessageListViewStateCallback
import com.github.vasniktel.snooper.util.ViewState

interface UserListViewStateCallback {
    fun onLoadingVisibilityChange(visible: Boolean)
    fun onDataLoaded(data: List<User>)
    fun onError(message: String, throwable: Throwable?)
    fun onPopulateState()
}

typealias UserListViewState = ViewState<UserListViewStateCallback>

data class Loading(
    private val active: Boolean
) : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onLoadingVisibilityChange(active)
    }
}

data class DataLoaded(
    private val data: List<User>
) : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onDataLoaded(data)
    }
}

data class ErrorState(
    private val message: String,
    private val throwable: Throwable? = null
): UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onError(message, throwable)
    }
}

object PopulateState : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onPopulateState()
    }
}
