package com.github.vasniktel.snooper.ui.messagelist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vasniktel.snooper.logic.message.MessageRepository
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.ui.messagelist.*
import com.github.vasniktel.snooper.util.doWork
import com.github.vasniktel.snooper.util.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class MessageListViewModel(
    protected val messageRepository: MessageRepository
) : MviViewModel<MessageListViewEvent, MessageListViewState>,
    MessageListViewEventCallback, ViewModel() {
    protected val _viewState = MutableLiveData<MessageListViewState>(
        PopulateState(false)
    )
    override val viewState: LiveData<MessageListViewState> = _viewState

    protected fun loadData(loader: suspend () -> List<Message>) {
        viewModelScope.doWork(
            mainContext = Dispatchers.Main,
            workContext = Dispatchers.IO,
            pre = { _viewState.value =
                LoadingState
            },
            worker = loader,
            post = {
                _viewState.value =
                    DataState(it)
            },
            onError = {
                _viewState.apply {
                    value =
                        ErrorState(
                            "Unable to load data",
                            it
                        )
                    value =
                        DataState(
                            emptyList()
                        )
                }
            }
        )
    }

    override fun onEvent(event: MessageListViewEvent) {
        event.applyCallback(this)
    }

    override fun onLikeClickedEvent(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addLike(message)
            withContext(Dispatchers.Main) {
                _viewState.value =
                    PopulateState(true)
            }
        }
    }
}
