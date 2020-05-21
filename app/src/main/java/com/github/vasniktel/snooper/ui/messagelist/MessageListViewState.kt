package com.github.vasniktel.snooper.ui.messagelist

import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.util.ViewState

interface MessageListViewStateCallback {
    fun onLoadingTopVisibilityChange(visible: Boolean)
    fun onLoadingBottomVisibilityChange(visible: Boolean)
    fun onNextPageLoaded(page: List<Message>)
    fun onNewDataLoaded(data: List<Message>)
    fun onError(message: String, throwable: Throwable?)
}

typealias MessageListViewState = ViewState<MessageListViewStateCallback>

data class LoadingTopActive(
    private val active: Boolean
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onLoadingTopVisibilityChange(active)
    }
}

data class LoadingBottomActive(
    private val active: Boolean
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onLoadingBottomVisibilityChange(active)
    }
}

data class LoadedPage(
    private val data: List<Message>
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onNextPageLoaded(data)
    }
}

data class LoadedFresh(
    private val data: List<Message>
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onNewDataLoaded(data)
    }
}

data class ErrorState(
    private val message: String,
    private val throwable: Throwable? = null
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onError(message, throwable)
    }
}
