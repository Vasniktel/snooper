package com.github.vasniktel.snooper.ui.userlist

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.mvi.ViewState

interface UserListViewStateCallback {
    fun onLoadingState()
    fun onDataState(data: List<User>)
    fun onErrorState(message: String, throwable: Throwable?)
    fun onPopulateState()
}

typealias UserListViewState = ViewState<UserListViewStateCallback>

object LoadingState : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onLoadingState()
    }
}

data class DataState(
    private val data: List<User>
) : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onDataState(data)
    }
}

data class ErrorState(
    private val message: String,
    private val throwable: Throwable? = null
): UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onErrorState(message, throwable)
    }
}

object PopulateState : UserListViewState {
    override fun applyCallback(callback: UserListViewStateCallback) {
        callback.onPopulateState()
    }
}
