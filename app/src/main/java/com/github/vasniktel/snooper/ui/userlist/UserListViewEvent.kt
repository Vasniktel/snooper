package com.github.vasniktel.snooper.ui.userlist

import com.github.vasniktel.snooper.logic.model.User
import com.github.vasniktel.snooper.util.mvi.ViewEvent

interface UserListViewEventCallback {
    fun onRefreshEvent(user: User?, fetch: Boolean)
}

typealias UserListViewEvent = ViewEvent<UserListViewEventCallback>

data class RefreshEvent(
    private val user: User?,
    private val fetch: Boolean
) : UserListViewEvent {
    override fun applyCallback(callback: UserListViewEventCallback) {
        callback.onRefreshEvent(user, fetch)
    }
}
