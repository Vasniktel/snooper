package com.github.vasniktel.snooper.ui.messagelist

import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.util.mvi.ViewState

interface MessageListViewStateCallback {
    fun onLoadingState()
    fun onDataState(data: List<Message>)
    fun onError(message: String, throwable: Throwable?)
    fun onPopulateState()
}

typealias MessageListViewState = ViewState<MessageListViewStateCallback>

object LoadingState : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onLoadingState()
    }
}

data class DataState(
    private val data: List<Message>
) : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onDataState(data)
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

object PopulateState : MessageListViewState {
    override fun applyCallback(callback: MessageListViewStateCallback) {
        callback.onPopulateState()
    }
}
